(function() {
    //1：红色预警 2：橙色预警 3：黄色预警
    var app = new QualityChecking({
        nanhaiMap: "../../images/china-south-sea.png",
        legendMap: "../../images/warningLegend.png",
        data: [
            { id: 110000, val1: 110000, val2: 1000, quality: 1 },
            { id: 120000, val1: 120000, val2: 100, quality: 2 },
            { id: 140000, val1: 140000, val2: 500, quality: 3 },
            { id: 150000, val1: 150000, val2: 800, quality: 1 },
            { id: 630000, val1: 630000, val2: 900, quality: 3 }
        ]
    });
    function window_resizeHandler() {
        app.resize(window.innerWidth - 20, window.innerHeight - 20);
    }

    function window_blurHandler() {
        app.stop();
    }

    function window_focusHandler() {
        app.start();
    }

    window.addEventListener("resize", window_resizeHandler);
    window.addEventListener("blur", window_blurHandler);
    window.addEventListener("focus", window_focusHandler);

    document.body.appendChild(app.domElement);
    window_resizeHandler();

    app.appear();
})();