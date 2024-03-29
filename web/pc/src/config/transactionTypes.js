const types = {
  //注释部分为前期交易类型，前期处理交易类型已有相应组件故未用到枚举，后期代码可根据枚举类型优化。
  // Send: {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   amount: { name: "amount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  // },
  // "Begin Redelegate": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   validator_src_address: { name: "validator_src_address", value: "" },
  //   validator_dst_address: { name: "validator_dst_address", value: "" },
  //   amount: { name: "amount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  // },
  // "Withdraw Delegator Reward": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   amount: { name: "amount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   rightCoinName: { name: "rightCoinName", value: "" },
  //   rightAmount: { name: "rightAmount", value: "" },
  // },
  // "Create Validator": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   selfDelegateAmount: { name: "selfDelegateAmount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   description: { name: "description", value: "" },
  //   commission: { name: "commission", value: "" },
  //   min_self_delegation: { name: "min_self_delegation", value: "" },
  //   consensus_pubkey: { name: "consensus_pubkey", value: "" },
  //   value: { name: "value", value: "" },
  // },
  // Delegate: {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   amount: { name: "amount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   AutoClaimReward: { name: "AutoClaimReward", value: "" },
  // },
  // Deposit: {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   tx_type: { name: "tx_type", value: "" },
  //   title: { name: "title", value: "" },
  //   proposal_id: { name: "proposal_id", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Edit Validator": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   commission_rate: { name: "commission_rate", value: "" },
  //   description: { name: "description", value: "" },
  //   min_self_delegation: { name: "min_self_delegation", value: "" },
  // },
  // "Multi Send": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  //   to: { name: "to", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Submit Proposal": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   tx_type: { name: "tx_type", value: "" },
  //   title: { name: "title", value: "" },
  //   description: { name: "description", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   initial_deposit: { name: "initial_deposit", value: "" },
  //   proposal_id: { name: "proposal_id", value: "" },
  // },
  // Transfer: {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   amount: { name: "amount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  // },
  // Undelegate: {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   amount: { name: "amount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   AutoClaimReward: { name: "AutoClaimReward", value: "" },
  // },
  // Unjail: {
  //   type: { name: "type", value: "" },
  //   to: { name: "to", value: "" },
  // },
  // Vote: {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   tx_type: { name: "tx_type", value: "" },
  //   title: { name: "title", value: "" },
  //   proposal_id: { name: "proposal_id", value: "" },
  //   option: { name: "option", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Withdraw Validator Commission": {
  //   type: { name: "type", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   from: { name: "from", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Issue Token": {
  //   type: { name: "type", value: "" },
  //   name: { name: "name", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   symbol: { name: "symbol", value: "" },
  //   initial_supply: { name: "initial_supply", value: "" },
  //   smallest_unit: { name: "smallest_unit", value: "" },
  //   decimals: { name: "decimals", value: "" },
  //   total_supply: { name: "total_supply", value: "" },
  //   mintable: { name: "mintable", value: "" },
  //   from: { name: "from", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Unlock Token": {
  //   type: { name: "type", value: "" },
  //   symbol: { name: "symbol", value: "" },
  //   from: { name: "from", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Transfer Token Owner": {
  //   type: { name: "type", value: "" },
  //   symbol: { name: "symbol", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Mint Token": {
  //   type: { name: "type", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  //   symbol: { name: "symbol", value: "" },
  //   to: { name: "to", value: "" },
  //   from: { name: "from", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Create Pool": {
  //   type: { name: "type", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   pledge: { name: "pledge", value: "" },
  //   defi_address: { name: "defi_address", value: "" },
  //   from: { name: "from", value: "" },
  //   pool_type_id: { name: "pool_type_id", value: "" },
  //   poolAddress: { name: "poolAddress", value: "" },
  //   valueAddedTaxAddress: { name: "valueAddedTaxAddress", value: "" },
  //   commissionRate: { name: "commissionRate", value: "" },
  //   commissionAddress: { name: "commissionAddress", value: "" },
  // },
  // "Add Pledge": {
  //   type: { name: "type", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  //   from: { name: "from", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Edit Token": {
  //   type: { name: "type", value: "" },
  //   mintable: { name: "mintable", value: "" },
  //   symbol: { name: "symbol", value: "" },
  //   from: { name: "from", value: "" },
  // },
  // "Place Order": {
  //   type: { name: "type", value: "" },
  //   pool_address: { name: "pool_address", value: "" },
  //   from: { name: "from", value: "" },
  //   expect_asset: { name: "expect_asset", value: "" },
  //   order_id: { name: "order_id", value: "" },
  //   my_asset: { name: "my_asset", value: "" },
  //   price: { name: "price", value: "" },
  // },
  // "Agree Order Pair": {
  //   type: { name: "type", value: "" },
  //   pool_address: { name: "pool_address", value: "" },
  //   from: { name: "from", value: "" },
  //   tx_pair: { name: "tx_pair", value: "" },
  //   left_order_id: { name: "left_order_id", value: "" },
  //   right_order_id: { name: "right_order_id", value: "" },
  //   price: { name: "price", value: "" },
  //   coinName: { name: "coinName", value: "" },
  // },
  // "Set Withdraw Address": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  // },
  // "Create Client": {
  //   type: { name: "type", value: "" },
  //   chain_id: { name: "chain_id", value: "" },
  //   trusting_period: { name: "trusting_period", value: "" },
  //   unbonding_period: { name: "unbonding_period", value: "" },
  //   signer: { name: "signer", value: "" },
  //   client_id: { name: "client_id", value: "" },
  //   client_type: { name: "client_type", value: "" },
  // },
  // "Update Client": {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   client_id: { name: "client_id", value: "" },
  //   height: { name: "height", value: "" },
  //   app: { name: "app", value: "" },
  //   block: { name: "block", value: "" },
  //   app_hash: { name: "app_hash", value: "" },
  //   chain_id: { name: "chain_id", value: "" },
  //   time: { name: "time", value: "" },
  //   data_hash: { name: "data_hash", value: "" },
  //   evidence_hash: { name: "evidence_hash", value: "" },
  //   hash: { name: "hash", value: "" },
  //   total: { name: "total", value: "" },
  //   consensus_hash: { name: "consensus_hash", value: "" },
  //   validators_hash: { name: "validators_hash", value: "" },
  //   last_commit_hash: { name: "last_commit_hash", value: "" },
  //   proposer_address: { name: "proposer_address", value: "" },
  //   last_results_hash: { name: "last_results_hash", value: "" },
  //   next_validators_hash: { name: "next_validators_hash", value: "" },
  // },
  // "Connection Open Confirm": {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   proof_ack: { name: "proof_ack", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  //   connection_id: { name: "connection_id", value: "" },
  // },
  // "Connection Open Init": {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   connection_id: { name: "connection_id", value: "" },
  //   counterparty_client_id: { name: "counterparty_client_id", value: "" },
  //   client_id: { name: "client_id", value: "" },
  // },
  // "Channel Open Init": {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   port_id: { name: "port_id", value: "" },
  //   state: { name: "state", value: "" },
  //   ordering: { name: "ordering", value: "" },
  //   version: { name: "version", value: "" },
  //   channel_id: { name: "channel_id", value: "" },
  //   connection_hops: { name: "connection_hops", value: "" },
  // },
  // Timeout: {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   data: { name: "data", value: "" },
  //   sequence: { name: "sequence", value: "" },
  //   source_port: { name: "source_port", value: "" },
  //   source_channel: { name: "source_channel", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  //   destination_port: { name: "destination_port", value: "" },
  //   timeout_timestamp: { name: "timeout_timestamp", value: "" },
  //   destination_channel: { name: "destination_channel", value: "" },
  //   proof_unreceived: { name: "proof_unreceived", value: "" },
  //   next_sequence_recv: { name: "next_sequence_recv", value: "" },
  // },
  // "Channel Open Ack": {
  //   type: { name: "type", value: "" },
  //   port_id: { name: "port_id", value: "" },
  //   channel_id: { name: "channel_id", value: "" },
  //   counterparty_channel_id: { name: "counterparty_channel_id", value: "" },
  //   counterparty_version: { name: "counterparty_version", value: "" },
  //   proof_try: { name: "proof_try", value: "" },
  //   signer: { name: "signer", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  // },
  // "Connection Open Ack": {
  //   type: { name: "type", value: "" },
  //   connection_id: { name: "connection_id", value: "" },
  //   counterparty_connection_id: {
  //     name: "counterparty_connection_id",
  //     value: "",
  //   },
  //   signer: { name: "signer", value: "" },
  //   version: { name: "version", value: "" },
  //   proof_client: { name: "proof_client", value: "" },
  //   proof_consensus: { name: "proof_consensus", value: "" },
  //   proof_try: { name: "proof_try", value: "" },
  //   aiTeType: { name: "aiTeType", value: "" },
  //   chain_id: { name: "chain_id", value: "" },
  //   proof_specs: { name: "proof_specs", value: "" },
  //   numerator: { name: "numerator", value: "" },
  //   denominator: { name: "denominator", value: "" },
  //   upgrade_path: { name: "upgrade_path", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  //   max_clock_drift: { name: "max_clock_drift", value: "" },
  //   trusting_period: { name: "trusting_period", value: "" },
  //   unbonding_period: { name: "unbonding_period", value: "" },
  //   allow_update_after_expiry: { name: "allow_update_after_expiry", value: "" },
  //   allow_update_after_misbehaviour: {
  //     name: "allow_update_after_misbehaviour",
  //     value: "",
  //   },
  // },
  // "Connection Open Try": {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   client_id: { name: "client_id", value: "" },
  //   proof_init: { name: "proof_init", value: "" },
  //   aiTeType: { name: "aiTeType", value: "" },
  //   chain_id: { name: "chain_id", value: "" },
  //   proof_specs: { name: "proof_specs", value: "" },
  //   numerator: { name: "numerator", value: "" },
  //   denominator: { name: "denominator", value: "" },
  //   upgrade_path: { name: "upgrade_path", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  //   max_clock_drift: { name: "max_clock_drift", value: "" },
  //   trusting_period: { name: "trusting_period", value: "" },
  //   unbonding_period: { name: "unbonding_period", value: "" },
  //   allow_update_after_expiry: { name: "allow_update_after_expiry", value: "" },
  //   allow_update_after_misbehaviour: {
  //     name: "allow_update_after_misbehaviour",
  //     value: "",
  //   },
  //   key_prefix: { name: "key_prefix", value: "" },
  //   delay_period: { name: "delay_period", value: "" },
  //   proof_client: { name: "proof_client", value: "" },
  //   proof_consensus: { name: "proof_consensus", value: "" },
  //   counterparty_versions: { name: "counterparty_versions", value: "" },
  //   previous_connection_id: { name: "previous_connection_id", value: "" },
  // },
  // "Recv Packet": {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   data: { name: "data", value: "" },
  //   sequence: { name: "sequence", value: "" },
  //   source_port: { name: "source_port", value: "" },
  //   source_channel: { name: "source_channel", value: "" },
  //   destination_port: { name: "destination_port", value: "" },
  //   timeout_timestamp: { name: "timeout_timestamp", value: "" },
  //   destination_channel: { name: "destination_channel", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  //   proof_commitment: { name: "proof_commitment", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   amount: { name: "amount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  // },
  // "Channel Open Try": {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   state: { name: "state", value: "" },
  //   version: { name: "version", value: "" },
  //   ordering: { name: "ordering", value: "" },
  //   channel_id: { name: "channel_id", value: "" },
  //   connection_hops: { name: "connection_hops", value: "" },
  //   port_id: { name: "port_id", value: "" },
  //   previous_channel_id: { name: "previous_channel_id", value: "" },
  //   proof_init: { name: "proof_init", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  //   counterparty_version: { name: "counterparty_version", value: "" },
  // },
  // "Redeem Pledge": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   pledge: { name: "pledge", value: "" },
  // },
  // "Create Defi": {
  //   type: { name: "type", value: "" },
  //   moniker: { name: "moniker", value: "" },
  //   min_self_delegation: { name: "min_self_delegation", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  // },
  // "Burn Token": {
  //   type: { name: "type", value: "" },
  //   symbol: { name: "symbol", value: "" },
  //   from: { name: "from", value: "" },
  //   amount: { name: "amount", value: "" },
  //   coinName: { name: "coinName", value: "" },
  // },
  // "Mint Coin": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   coin: { name: "coin", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Burn Coin": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   coin: { name: "coin", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Loan Coin": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Revoke Order": {
  //   type: { name: "type", value: "" },
  //   pool_address: { name: "pool_address", value: "" },
  //   owner_address: { name: "owner_address", value: "" },
  //   tx_pair: { name: "tx_pair", value: "" },
  //   order_id: { name: "order_id", value: "" },
  //   is_left_order: { name: "is_left_order", value: "" },
  //   isValidator: { name: "isValidator", value: "" },
  // },
  // "Channel Open Confirm": {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   channel_id: { name: "channel_id", value: "" },
  //   port_id: { name: "port_id", value: "" },
  //   proof_ack: { name: "proof_ack", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  // },
  // Acknowledgement: {
  //   type: { name: "type", value: "" },
  //   signer: { name: "signer", value: "" },
  //   sequence: { name: "sequence", value: "" },
  //   source_port: { name: "source_port", value: "" },
  //   destination_port: { name: "destination_port", value: "" },
  //   destination_channel: { name: "destination_channel", value: "" },
  //   data: { name: "data", value: "" },
  //   revision_number: { name: "revision_number", value: "" },
  //   revision_height: { name: "revision_height", value: "" },
  //   acknowledgement: { name: "acknowledgement", value: "" },
  // },
  // "Defi Delegate": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Withdraw Defi Delegator Reward": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Defi Undelegate": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Swap Within Batch": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   offerCoin: { name: "offerCoin", value: "" },
  //   offerAmount: { name: "offerAmount", value: "" },
  //   feePoolAmount: { name: "feePoolAmount", value: "" },
  //   buyCoin: { name: "buyCoin", value: "" },
  //   buyAmount: { name: "buyAmount", value: "" },
  // },
  // "Deposit Within Batch": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   poolId: { name: "poolId", value: "" },
  //   leftCoin: { name: "leftCoin", value: "" },
  //   leftAmount: { name: "leftAmount", value: "" },
  //   rightCoin: { name: "rightCoin", value: "" },
  //   rightAmount: { name: "rightAmount", value: "" },
  //   txPairName: { name: "txPairName", value: "" },
  // },
  // "Withdraw Within Batch": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Set Orchestrator Address": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   orchestrator: { name: "orchestrator", value: "" },
  //   ethAddress: { name: "ethAddress", value: "" },
  // },
  // "Valset Confirm": {
  //   type: { name: "type", value: "" },
  //   nonce: { name: "nonce", value: "" },
  //   orchestrator: { name: "orchestrator", value: "" },
  //   ethAddress: { name: "ethAddress", value: "" },
  //   signature: { name: "signature", value: "" },
  // },
  // "Deposit Claim": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   ethBlockHeight: { name: "ethBlockHeight", value: "" },
  //   eventNonce: { name: "eventNonce", value: "" },
  //   contractAddress: { name: "contractAddress", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  // },
  // "Valset Updated Claim": {
  //   type: { name: "type", value: "" },
  //   ethBlockHeight: { name: "ethBlockHeight", value: "" },
  //   eventNonceto: { name: "eventNonce", value: "" },
  //   valsetNonce: { name: "valsetNonce", value: "" },
  //   eventethereum_addresNonce: { name: "ethereum_addres", value: "" },
  //   orchestrator: { name: "orchestrator", value: "" },
  // },
  // "Send To Eth": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   to: { name: "to", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   amount: { name: "amount", value: "" },
  //   bridgeFee: { name: "bridgeFee", value: "" },
  //   outgoingTxId: { name: "outgoingTxId", value: "" },
  // },
  // "Confirm Batch": {
  //   type: { name: "type", value: "" },
  //   contractAddress: { name: "contractAddress", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   from: { name: "from", value: "" },
  //   nonce: { name: "nonce", value: "" },
  //   orchestrator: { name: "orchestrator", value: "" },
  //   signature: { name: "signature", value: "" },
  // },
  // "Request Batch": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "" },
  //   contractAddress: { name: "contractAddress", value: "" },
  //   coinName: { name: "coinName", value: "" },
  //   batchNonce: { name: "batchNonce", value: "" },
  // },
  // "Withdraw Claim": {
  //   type: { name: "type", value: "" },
  //   eventNonce: { name: "eventNonce", value: "" },
  //   ethBlockHeight: { name: "ethBlockHeight", value: "" },
  //   contractAddress: { name: "contractAddress", value: "" },
  //   orchestrator: { name: "orchestrator", value: "" },
  //   bathNonce: { name: "bathNonce", value: "" },
  // },
  // "Mint Nft": {
  //   type: { name: "type", value: "" },
  //   from: { name: "from", value: "", link: "/Accountbalance/" },
  //   to: { name: "to", value: "", link: "/Accountbalance/" },
  //   tokenId: { name: "tokenId", value: "" },
  //   cateId: { name: "cateId", value: "" },
  //   tokenUri: { name: "tokenUri", value: "" },
  //   companyUri: { name: "companyUri", value: "" },
  //   valueAddedTax: { name: "valueAddedTax", value: "" },
  //   copyrightTax: { name: "copyrightTax", value: "" },
  // },

  //划分一下
  Bid: {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    tokenId: { name: "tokenId", value: "" },
    poolAddress: { name: "poolAddress", value: "" },
    isFixedPrice: { name: "isFixedPrice", value: false },
    // coinName: { name: "coinName", value: "" },
    amount: { name: "amount", value: 0 },
  },
  "Create Order": {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    tokenId: { name: "tokenId", value: "" },
    cateId: { name: "cateId", value: "" },
    poolAddress: { name: "poolAddress", value: "" },
    isFixedPrice: { name: "isFixedPrice", value: false },
    // coinName: { name: "coinName", value: "" },
    amount: { name: "amount", value: 0 },
  },
  "Mint Batch": {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    to: { name: "to", value: "", link: "/Accountbalance/" },
    items: { name: "items", value: "" },
  },
  "Frozen Nft": {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    cateId: { name: "cateId", value: "" },
    tokenId: { name: "tokenId", value: "" },
  },
  "Transfer Nft": {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    to: { name: "to", value: "", link: "/Accountbalance/" },
    cateId: { name: "cateId", value: "" },
    tokenId: { name: "tokenId", value: "" },
  },
  "Delete Order": {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    poolAddress: { name: "poolAddress", value: "" },
    isFixedPrice: { name: "isFixedPrice", value: false },
    tokenId: { name: "tokenId", value: "" },
  },
  "Agree Order": {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    poolAddress: { name: "poolAddress", value: "" },
    tokenId: { name: "tokenId", value: "" },
  },
  "Update Order": {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    poolAddress: { name: "poolAddress", value: "" },
    tokenId: { name: "tokenId", value: "" },
    // coinName: { name: "coinName", value: "" },
    amount: { name: "amount", value: 0 },
  },
  "Update Pool": {
    type: { name: "type", value: "" },
    from: { name: "from", value: "", link: "/Accountbalance/" },
    poolAddress: { name: "poolAddress", value: "" },
    commissionAddress: { name: "commissionAddress", value: "" },
    valueAddedTaxAddress: { name: "valueAddedTaxAddress", value: "" },
    commissionRate: { name: "commissionRate", value: "" },
  },
};

export default types;
