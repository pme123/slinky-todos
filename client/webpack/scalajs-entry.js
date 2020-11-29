if (process.env.NODE_ENV === "production") {
    const opt = require("./scalably-slinky-example-opt.js");
    opt.main();
    module.exports = opt;
} else {
    var exports = window;
    exports.require = require("./scalably-slinky-example-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./scalably-slinky-example-fastopt.js");
    fastOpt.main()
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
