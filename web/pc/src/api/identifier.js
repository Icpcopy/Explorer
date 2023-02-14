import request from '@/utils/request'
export function getValidatorsByStatus(data) {
    return request({
      url: '/api/validator/getValidatorsByStatus',
      method: 'get',
      params:data
    })
  }

  export function getCommissionByMoniker(data) {
    return request({
      url: '/api/validator/getOtherValidatorCommissionByMoniker',
      method: 'get',
      params:data
    })
  }
  export function getValidatorsByMoniker(data) {
    return request({
      url: '/api/validator/getValidatorsByMoniker',
      method: 'get',
      params:data
    })
  }

  export function getDelegatorByOperatorAddress(data) { // 名下委托人列表
    return request({
      url: '/api/validator/getDelegatorByOperatorAddress',
      method: 'get',
      params:data
    })
  }
  export function getValidatorRelatedTx(data) { // 名下委托人列表
    return request({
      url: '/api/transaction/getValidatorRelatedTx',
      method: 'get',
      params:data
    })
  }
  export function getUnDelegatorByOperatorAddress(data) { // 名下解除的委托人列表
    return request({
      url: '/api/validator/getUnDelegatorByOperatorAddress',
      method: 'get',
      params:data
    })
  }

