<template xmlns:a-col="http://www.w3.org/1999/html">
  <a-row class="login">
    <a-col :span="8" :offset="8" class="login-main">
      <h1 style="text-align: center"><rocket-two-tone />&nbsp;甲蛙12306售票系统</h1>
      <a-form
          :model="loginForm"
          name="basic"
          autocomplete="off"
          @finish="onFinish"
          @finishFailed="onFinishFailed"
      >
        <a-form-item
            label=""
            name="mobile"
            :rules="[{ required: true, message: '请输入手机号!' }]"
        >
          <a-input v-model:value="loginForm.mobile" style="width: calc(100%)"/>
        </a-form-item>

        <a-form-item
            label=""
            name="code"
            :rules="[{ required: true, message: '请输入验证码!' }]"
        >
          <a-input-group compact>
            <a-input v-model:value="loginForm.code" style="width: calc(100% - 108px)" />
            <a-button @click="sendCode" type="primary">获取验证码</a-button>
          </a-input-group>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" block @click="login">登录</a-button>
        </a-form-item>
      </a-form>
    </a-col>
  </a-row>
</template>
<script>
import { defineComponent,reactive } from 'vue';
import { notification } from 'ant-design-vue';
import axios from "axios";
import {useRouter} from "vue-router";
import store from "@/store";

export default defineComponent({
  name:"login-view",
  setup(){
    const router = useRouter();
    const loginForm = reactive({
      mobile: '15795650358',
      code: '',
    });
    const sendCode = () => {
      axios.post("/member/member/send-code",{
        mobile: loginForm.mobile
      }).then((response) =>{
        let data = response.data;
        if (data.success) {
          notification.success({ description: '发送验证码成功！' });
          loginForm.code = "1234";
        } else {
          notification.error({ description: data.message });
        }
      })
    };
    const login = () => {
      axios.post("/member/member/login", loginForm).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '登录成功！' });
          // 登录成功，跳到控台主页
          //router.push('/');
          router.push("/welcome");
          store.commit("setMember", data.content);
        } else {
          notification.error({ description: data.message });
        }
      })
    };
    return {
      loginForm,
      login,
      sendCode
    }
  }
});
</script>
<style>
.login-main h1 {
  font-size: 25px;
  font-weight: bold;
}
.login-main {
  margin-top: 100px;
  padding: 30px 30px 20px;
  border: 2px solid grey;
  border-radius: 10px;
  background-color: #fcfcfc;
}
</style>