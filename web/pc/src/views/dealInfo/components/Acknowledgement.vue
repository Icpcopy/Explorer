<template>
  <div class="container">
    <section class="section-top" style="margin-top:30px">
      <div class="s-title">Transaction Message or Result</div>
      <div class="content displaywrap">
        <div>
          <span class="titles">TxType:</span>
          <div>{{ data.type }}</div>
        </div>
        <div>
          <span class="titles">Signer:</span>
          <div>
            <span class="maodian" @click="$headerRouterFn(data.signer)">
              {{
              data.signer
              }}
            </span>
          </div>
        </div>
        <div>
          <span class="titles">Sequence:</span>
          <div>{{ data.sequence }}</div>
        </div>
        <div>
          <span class="titles">Source_port:</span>
          <div>{{ data.source_port }}</div>
        </div>
        <div>
          <span class="titles">Destination_port:</span>
          <div>{{ data.destination_port }}</div>
        </div>
        <div>
          <span class="titles">Destination_channel:</span>
          <div>{{ data.destination_channel }}</div>
        </div>
        <div>
          <span class="titles">Data:</span>
          <div>{{ data.data }}</div>
        </div>
        <div>
          <span class="titles">Revision_number:</span>
          <div>{{ data.revision_number }}</div>
        </div>
        <div>
          <span class="titles">Revision_height:</span>
          <div>{{ data.revision_height }}</div>
        </div>

        <div>
          <span class="titles">Acknowledgement:</span>
          <div>{{ data.acknowledgement }}</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  props: ["data"],
  data() {
    return {};
  },
  components: {}
};
</script>

<style scoped lang="scss">
</style>
