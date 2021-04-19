const Json2csvParser = require('json2csv').Parser

module.exports = class ReportGenerator {
  constructor(data) {
    this.data = data
  }

  generateOverviewCSV() {
    const fields = [
      {
        label: 'Customer Count',
        value: 'customerCount',
      },
      {
        label: 'Policy Count',
        value: 'policyCount',
      },
      {
        label: 'Avg. Deductible',
        value: 'avgDeductible',
      },
      {
        label: 'Avg. Insurance Premium',
        value: 'avgInsurancePremium',
      },
      {
        label: 'Avg. Policy Limit',
        value: 'avgPolicyLimit',
      },
    ]

    let customerCount = 0
    let policyCount = 0
    let deductibleTotal = 0
    let insurancePremiumTotal = 0
    let policyLimitTotal = 0

    const customers = Object.values(this.data)
    for (const customer of customers) {
      customerCount += 1

      const policies = Object.values(customer.policies)
      for (const policy of policies) {
        policyCount += 1
        deductibleTotal += policy.deductible.amount
        insurancePremiumTotal += policy.insurancePremium.amount
        policyLimitTotal += policy.policyLimit.amount
      }
    }

    const avgDeductible =
      policyCount > 0 ? (deductibleTotal / policyCount).toFixed() + ' CHF' : '-'
    const avgInsurancePremium =
      policyCount > 0
        ? (insurancePremiumTotal / policyCount).toFixed() + ' CHF'
        : '-'
    const avgPolicyLimit =
      policyCount > 0
        ? (policyLimitTotal / policyCount).toFixed() + ' CHF'
        : '-'

    const data = {
      customerCount,
      policyCount,
      avgDeductible,
      avgInsurancePremium,
      avgPolicyLimit,
    }

    return this.createCSV(fields, data)
  }

  getAgeGroupData() {
    const customers = Object.values(this.data)
    const now = new Date()
    let total = 0

    const ageGroups = [
      {
        ageGroup: '0 - 20',
        cutOff: 20,
        total: 0,
      },
      {
        ageGroup: '20 - 30',
        cutOff: 30,
        total: 0,
      },
      {
        ageGroup: '30 - 40',
        cutOff: 40,
        total: 0,
      },
      {
        ageGroup: '40 - 50',
        cutOff: 50,
        total: 0,
      },
      {
        ageGroup: '50 - 60',
        cutOff: 60,
        total: 0,
      },
      {
        ageGroup: '60 - 80',
        cutOff: 80,
        total: 0,
      },
      {
        ageGroup: '> 80',
        cutOff: Number.POSITIVE_INFINITY,
        total: 0,
      },
    ]

    for (const customer of customers) {
      const birthdayDate = new Date(customer.customerProfile.birthday)
      const ageInYears = (now - birthdayDate) / (1000 * 60 * 60 * 24 * 365)
      total += 1

      for (const ageGroup of ageGroups) {
        if (ageInYears < ageGroup.cutOff) {
          ageGroup.total += 1
          break
        }
      }
    }

    return ageGroups.map(ageGroup => ({
      ageGroup: ageGroup.ageGroup,
      percentage:
        total > 0 ? ((ageGroup.total / total) * 100).toFixed() + ' %' : '-',
      total: ageGroup.total,
    }))
  }

  generateAgeGroupCSV() {
    const ageGroupFields = [
      {
        label: 'Age Group',
        value: 'ageGroup',
      },
      {
        label: 'Percentage',
        value: 'percentage',
      },
      {
        label: 'Total',
        value: 'total',
      },
    ]

    const ageGroups = this.getAgeGroupData()
    return this.createCSV(ageGroupFields, ageGroups)
  }

  getRegionData() {
    const customers = Object.values(this.data)
    let total = 0

    const regions = [
      {
        region: 'Südwestschweiz',
        cutOff: 2000,
        total: 0,
      },
      {
        region: 'Nordwestschweiz',
        cutOff: 3000,
        total: 0,
      },
      {
        region: 'Bern / Oberwallis',
        cutOff: 4000,
        total: 0,
      },
      {
        region: 'Basel',
        cutOff: 5000,
        total: 0,
      },
      {
        region: 'Aargau',
        cutOff: 6000,
        total: 0,
      },
      {
        region: 'Zentralschweiz / Tessin',
        cutOff: 7000,
        total: 0,
      },
      {
        region: 'Graubünden',
        cutOff: 8000,
        total: 0,
      },
      {
        region: 'Zürich / Thurgau',
        cutOff: 9000,
        total: 0,
      },
      {
        region: 'Ostschweiz',
        cutOff: 10000,
        total: 0,
      },
    ]

    for (const customer of customers) {
      total += 1

      const postalCode = parseInt(customer.customerProfile.postalCode)
      if (isNaN(postalCode)) {
        continue
      }

      for (const region of regions) {
        if (postalCode < region.cutOff) {
          region.total += 1
          break
        }
      }
    }

    return regions.map(region => ({
      region: region.region,
      percentage:
        total > 0 ? ((region.total / total) * 100).toFixed() + ' %' : '-',
      total: region.total,
    }))
  }

  generateRegionCSV() {
    const regionFields = [
      {
        label: 'Region',
        value: 'region',
      },
      {
        label: 'Percentage',
        value: 'percentage',
      },
      {
        label: 'Total',
        value: 'total',
      },
    ]

    const regions = this.getRegionData()
    return this.createCSV(regionFields, regions)
  }

  createCSV(fields, data) {
    const parser = new Json2csvParser({
      fields,
      delimiter: ';',
      excelStrings: true,
    })
    return parser.parse(data)
  }

  generateCSV() {
    const utf8BOM = '\ufeff'
    const overviewCSV = this.generateOverviewCSV()
    const ageGroupCSV = this.generateAgeGroupCSV()
    const regionCSV = this.generateRegionCSV()
    return `${utf8BOM}${overviewCSV}\n\n${ageGroupCSV}\n\n${regionCSV}`
  }
}
