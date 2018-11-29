const nconf = require('nconf')
const path = require('path')
const program = require('commander')
const grpc = require('grpc')
const ProgressBar = require('progress')
const fs = require('fs')

nconf
  .argv()
  .env()
  .file({ file: path.join(__dirname, 'config.json') })

const grpc_config = nconf.get('gRPC')

function printReport(report, outputPath) {
  fs.writeFile(outputPath, report.csv, err => {
    if (err) {
      return console.log(err)
    }

    console.log(`Wrote results to ${outputPath}.`)
  })
}

program.command('run [output-path]').action(outputPath => {
  const PROTO_PATH = path.join(
    __dirname,
    '../risk-management-server/riskmanagement.proto'
  )
  const proto = grpc.load(PROTO_PATH).riskmanagement
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
  call.on('data', reply => {
    if (reply.report_or_progress === 'progress') {
      bar.tick()
    } else {
      if (outputPath) {
        printReport(reply.report, outputPath)
      } else {
        console.log(reply.report.csv)
      }
    }
  })

  call.on('end', () => {})
})

if (process.argv.length <= 2) {
  program.help()
} else {
  program.parse(process.argv)
}
