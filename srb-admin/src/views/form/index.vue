<template>
  <div class="app-container">
    <el-form>
      <el-select v-model="value" placeholder="选择需要抓取的模块">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-input class='fileName-input' v-model='fileName' :placeholder="'输入文件名称,默认为：'+this.value" :disabled='inputDisabled'></el-input>
      <el-button type='primary' class='btn-crawler' @click='crawler()'>{{this.crawlerContent}}</el-button>
      <el-button class='btn-all' type='primary' @click='fileOpen()'>打开爬虫所在的目录</el-button>
      <br>
      <span>{{messageTip}}</span>
    </el-form>
  </div>
</template>

<script>
  import common from '@/api/common'
export default {
  data() {
    return {
      messageTip: '',
      moduleValue: '',
      options: [{
        value: 'apis',
        label: 'API模块数据'
      }, {
        value: 'mashups',
        label: 'Mashups模块数据'
      }, {
        value: 'sampleSourceCode',
        label: 'SampleSourceCode模块数据'
      }, {
        value: 'sdks',
        label: 'SDK模块数据'
      },{
        value: '',
        label: '所有模块'
      }],
      value: '',
      crawlerContent: '抓取所有的模块',
      flag: false,
      inputDisabled: true,
      //用来自定义数据保存的文件名称
      fileName: ''
    }
  },
  watch:{
    value(){
      if(this.value==""){
        this.crawlerContent = "抓取所有的模块"
        this.flag = false
        this.inputDisabled = true
        this.fileName = ''
      }else{

        this.crawlerContent = "抓取"+this.value+"模块"
        this.flag = true
        this.inputDisabled = false
      }
    }
  },
  methods: {
    fileOpen(){
      this.messageTip = ""
      common.openDir().then(res=>{
        console.log(res)
        this.messageTip = res.data
      })
    },
    crawler(){
      if(this.fileName == ""){
        this.fileName = this.value;
      }
      common.executePython(this.value,this.fileName,this.flag).then(res=>{
        this.messageTip = res.data
      })

    }
  },

}
</script>

<style lang='scss'>
  el-select{

  }
  .btn-crawler{
    margin-left: 10px;
  }
  .fileName-input{
    margin-left: 10px;
    width: 330px;
  }
  .btn-all{
    margin: 10px;
  }
</style>

