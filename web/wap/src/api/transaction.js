
import request from '@/utils/request'

export function getValidatorRelatedTx(data) {
  return request({
    url: '/api/transaction/getValidatorRelatedTx',
    method: 'get',
    params:data
  })
}
