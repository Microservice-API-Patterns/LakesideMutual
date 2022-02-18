require('log-timestamp')(() => {
  const timestamp = new Date().toLocaleString('de-CH', {
    timeZone: 'Europe/Zurich',
  })
  return `[${timestamp}] %s`
})
const nconf = require('nconf')
const path = require('path')
const stompit = require('stompit')
const protoLoader = require('@grpc/proto-loader')
const grpc = require('@grpc/grpc-js')
const DataManager = require('./lib/data-manager')
const ReportGenerator = require('./lib/report-generator')

nconf
  .argv()
  .env({ lowerCase: true, separator: '_' })
  .file({ file: path.join(__dirname, 'config.json') })

/*
The handleMessage() function gets called whenever a new PolicyEvent message is available
on the message queue. Each message is then persisted with the data manager.
*/
function handleMessage(channel, dataManager, error, message, subscription) {
  if (error) {
    console.error(`Error: ${error}`)
    channel.close()
    return
  }

  message.readString('utf8', (error, string) => {
    if (error) {
      console.error(`Error: ${error}`)
      channel.close()
      return
    }

    const event = JSON.parse(string)
    console.log('An event has been consumed:')
    console.log(JSON.stringify(event, null, 4))
    dataManager.addEvent(event)
    dataManager
      .save()
      .then(() => {
        channel.ack(message)
      })
      .catch((error) => {
        console.error(`Error: ${error}`)
      })
  })
}

/*
The consumeEvents() function consumes PolicyEvent messages from the message queue when
they become available. Each event is then persisted with the data manager.
*/
function consumeEvents(dataManager) {
  const mq_config = nconf.get('activemq')
  console.log(
    'Starting to consume PolicyEvent messages from: ',
    mq_config.host,
    mq_config.port,
    mq_config.username
  )
  const connectOptions = {
    host: mq_config.host,
    port: mq_config.port,
    connectHeaders: {
      host: '/',
      login: mq_config.username,
      passcode: mq_config.password,
      'heart-beat': '5000,5000',
    },
  }

  const connectFailover = new stompit.ConnectFailover([connectOptions])

  connectFailover.on('error', (error) => {
    const connectArgs = error.connectArgs
    const address = `${connectArgs.host}:${connectArgs.port}`
    console.log(`Connection error to ${address}: ${error.message}`)
  })

  const channelFactory = new stompit.ChannelFactory(connectFailover)
  channelFactory.channel((error, channel) => {
    if (error) {
      console.log('channel factory error: ' + error.message)
      reject(error)
      return
    }

    const subscribeHeaders = {
      destination: `/queue/${mq_config.queueName}`,
      ack: 'client-individual',
    }

    const messageHandler = handleMessage.bind(null, channel, dataManager)
    channel.subscribe(subscribeHeaders, messageHandler)
  })
}

/*
The handleClientRequest() function gets called whenever a request from the Risk Management Client
is received. It then generates a customer data report and sends it back to the client.
 */
function handleClientRequest(dataManager, call, callback) {
  try {
    console.log('Received request from Risk Management Client.')

    let i = 0
    let theInterval = setInterval(() => {
      if (i > 100) {
        const reportGenerator = new ReportGenerator(dataManager.data)
        const csv = reportGenerator.generateCSV()
        const report = { csv }
        call.write({ report })
        call.end()
        console.log('Sent response to Risk Management Client.')
        clearInterval(theInterval)
      } else {
        const progress = i
        call.write({ progress })
      }
      i += 1
    }, 20)
  } catch (error) {
    console.error(`Error: ${error}`)
    callback(null, { csv: '' })
  }
}

/*
The startGRPCServer() function starts the gRPC server which listens for
requests from the Risk Management Client.
 */
function startGRPCServer(dataManager) {
  const grpc_config = nconf.get('grpc')
  const PROTO_PATH = path.join(__dirname, '/riskmanagement.proto')
  const packageDefinition = protoLoader.loadSync(PROTO_PATH)
  const proto = grpc.loadPackageDefinition(packageDefinition).riskmanagement
  const server = new grpc.Server()
  const requestHandler = handleClientRequest.bind(null, dataManager)
  server.addService(proto.RiskManagement.service, {
    trigger: requestHandler,
  })
  server.bindAsync(
    `${grpc_config.host}:${grpc_config.port}`,
    grpc.ServerCredentials.createInsecure(),
    () => {
      console.log(
        `Listening for requests from Risk Management Client on ${grpc_config.host}:${grpc_config.port}`
      )
      server.start()
    }
  )
}

process.on('unhandledRejection', (err) => {
  console.error(err)
})

const dataManager = new DataManager()
consumeEvents(dataManager)
startGRPCServer(dataManager)
