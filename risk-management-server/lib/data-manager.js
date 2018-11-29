const path = require('path')
const fs = require('fs')
const dataFilePath = path.join(__dirname, '../data/data.json')
const initialDataFilePath = path.join(__dirname, '../data/initial-data.json')

module.exports = class DataManager {
  constructor() {
    const filePath = fs.existsSync(dataFilePath)
      ? dataFilePath
      : initialDataFilePath
    const content = fs.readFileSync(filePath)
    this.data = JSON.parse(content)
  }

  addEvent(event) {
    this.data[event.customer.customerId] = {
      customerProfile: event.customer,
      activePolicy: event.policy,
    }
  }

  async save() {
    return new Promise((resolve, reject) => {
      const content = JSON.stringify(this.data)
      fs.writeFile(dataFilePath, content, error => {
        if (error) {
          reject(error)
        } else {
          resolve()
        }
      })
    })
  }
}
