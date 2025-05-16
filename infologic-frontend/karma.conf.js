module.exports = function (config) {
  config.set({
    frameworks: ["jasmine", "@angular-devkit/build-angular"],
    plugins: [
      require("karma-jasmine"),
      require("karma-chrome-launcher"),
      require("karma-coverage"), // Plugin de cobertura
      require("karma-jasmine-html-reporter"),
      require("@angular-devkit/build-angular/plugins/karma"),
    ],
    client: {
      clearContext: false, // Mantener la salida de las pruebas visible en el navegador
    },
    coverageReporter: {
      dir: require("path").join(__dirname, "./coverage/fact-electr-cnr"), // Ruta donde se generará el reporte de cobertura
      subdir: ".", // Subdirectorio para los informes de cobertura
      reporters: [
        { type: "html" }, // Informe HTML (visible en navegador)
        { type: "lcovonly" }, // Informe en formato lcov para SonarQube
        { type: "text-summary" }, // Resumen en la consola
      ],
    },
    reporters: ["progress", "kjhtml"],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    browsers: ["Chrome"],
    singleRun: false,
    restartOnFileChange: true,
  });
};
