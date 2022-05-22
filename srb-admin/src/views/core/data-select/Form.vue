<template>
  <div>
    <template>
      <div class="app-container">
        <!-- 输入表单 -->
        <el-form label-width="120px">
          <el-form-item label="借款额度">
            <el-input-number v-model="integralGrade.borrowAmount" :min="0" />
          </el-form-item>
          <el-form-item label="积分区间开始">
            <el-input-number v-model="integralGrade.integralStart" :min="0" />
          </el-form-item>
          <el-form-item label="积分区间结束">
            <el-input-number v-model="integralGrade.integralEnd" :min="0" />
          </el-form-item>
          <el-form-item>
            <el-button
              :disabled="saveBtnDisabled"
              type="primary"
              @click="saveOrUpdate()"
            >
              保存
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </template>
  </div>
</template>

<script>
import integralGradeApi from '@/api/core/mashups'
export default {
  name: 'Form',
  data(){
    return {
      integralGrade: {
      }, //初始化数据
      saveBtnDisabled: false //防止表单的重复提交
    }
  },
  //页面渲染成功
  created() {
    if (this.$route.params.id) {
      this.fetchDataById(this.$route.params.id)
    }
  },
  methods: {
    saveOrUpdate(){
      //禁用保存按钮
      this.saveBtnDisabled = true

      //如果已经提交过啦，然后修改了某些数据，只需要更新即可，不需要发送保存请求
      if(!this.integralGrade.id){
        this.saveData()
      }else{
        this.updateData()
      }
      // this.saveData()
    },
    saveData(){
      integralGradeApi.save(this.integralGrade).then(response => {
        this.$message({
          type: 'success',
          message: response.message
        })
        this.$router.push('/core/data-select/list')
      })
    },
    fetchDataById(id){
      integralGradeApi.getById(id).then(response => {
        this.integralGrade = response.data.integralGrade
      })
    },
    updateData(){
      //数据获取
      integralGradeApi.updateById(this.integralGrade).then(response => {
        this.$message({
          type: 'success',
          message: response.message
        })
        this.$router.push('/core/data-select/list')
      })
    }
  }
}
</script>

<style scoped>

</style>
