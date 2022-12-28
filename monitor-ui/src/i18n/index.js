import { createI18n } from "vue-i18n";
// 语言包
import zhCn from "./lang/zh-CN";
import en from "./lang/en-US";
 
const i18n = createI18n({
  locale: (localStorage.getItem("locale") == "en-US" ? "en" : "zhCn") || "zhCn",
  messages: {
    zhCn,
    en,
  },
});
 
export default i18n;