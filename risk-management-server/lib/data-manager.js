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
    if (event.kind === 'UpdatePolicyEvent') {
      this.handleUpdatePolicyEvent(event)
    } else if (event.kind === 'DeletePolicyEvent') {
      this.handleDeletePolicyEvent(event)
    }
  }

  handleUpdatePolicyEvent(event) {
    const customerId = event.customer.customerId
    const customerData = {
      customerProfile: event.customer,
      policies: (this.data[customerId] && this.data[customerId].policies) || {},
    }
    customerData.policies[event.policy.policyId] = event.policy
    this.data[customerId] = customerData
  }

  handleDeletePolicyEvent(event) {
    const policyId = event.policyId
    const customers = Object.values(this.data)
    for (const customer of customers) {
      delete customer.policies[policyId]
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
