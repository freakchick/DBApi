const install = (Vue) => {
    const requireComponent = require.context(".", true, /\.vue/);

    requireComponent.keys().forEach(fileName => {
        const config = requireComponent(fileName);
        Vue.component(config.default.name, config.default);
    })
}
export default {
    install
}
