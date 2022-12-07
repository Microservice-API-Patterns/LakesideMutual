const nconf = require('nconf')
const path = require('path')
const program = require('commander')
const protoLoader = require('@grpc/proto-loader')
const grpc = require('@grpc/grpc-js')
const ProgressBar = require('progress')
const fs = require('fs')

nconf
  .argv()
  .env()
  .file({ file: path.join(__dirname, 'config.json') })

const grpc_config = nconf.get('gRPC')

function printReport(report, outputPath) {
  fs.writeFileSync(outputPath, report.csv)
  console.log(`Wrote results to ${outputPath}.`)
}

program.command('run [output-path]').action((outputPath) => {
  const PROTO_PATH = path.join(
    __dirname,
    '../risk-management-server/riskmanagement.proto'
  )

  const packageDefinition = protoLoader.loadSync(PROTO_PATH)
  const proto = grpc.loadPackageDefinition(packageDefinition).riskmanagement

  const client = new proto.RiskManagement(
    `${grpc_config.host}:${grpc_config.port}`,
    grpc.credentials.createInsecure()
  )

  const bar = new ProgressBar('Processing: [:bar] :percent', {
    total: 100,
    width: 50,
    clear: false,
  })

  const call = client.trigger({})
  call.on('data', (reply) => {
    if (reply.progress != undefined) {
      bar.tick()
    } else {
      if (outputPath) {
        printReport(reply.report, outputPath)
      } else {
        console.log(reply.report.csv)
      }
      process.exit()
    }
  })

  call.on('end', () => {})
})

if (process.argv.length <= 2) {
  program.help()
} else {
  program.parse(process.argv)
}
