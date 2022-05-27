module.exports = {
    devServer: {
        disableHostCheck: true,
        public: '0.0.0.0:8521',
        proxy: "http://127.0.0.1:8520", //开发环境的跨域问题解决
        port: 8521
    }
}
