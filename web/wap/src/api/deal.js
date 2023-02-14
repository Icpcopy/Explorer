
import request from '@/utils/request'

  export function getTransactionByHash(data) {
    return request({
      url: '/api/transaction/getTransactionByHash',
      method: 'get',
      params:data
    })
  }

  export function getByBlockNumber(data) {
    return request({
      url: '/api/transaction/getTransactionByBlockNumber',
      method: 'get',
      params:data
    })
  }

  export function getAddressInfo(data) {
    return request({
      url: '/api/address/getAddressInfo',
      method: 'get',
      params:data
    })
  }

  export function getAddressDelegateInfo(data) {
    return request({
      url: '/api/address/getAddressDelegateInfo',
      method: 'get',
      params:data
    })
  }