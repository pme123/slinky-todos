if (process.env.NODE_ENV === "production") {
    const opt = require("./slinky-todos-client-opt.js");
    opt.main();
    module.exports = opt;
} else {
    var exports = window;
    exports.require = require("./slinky-todos-client-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./slinky-todos-client-fastopt.js");
    fastOpt.main()
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
