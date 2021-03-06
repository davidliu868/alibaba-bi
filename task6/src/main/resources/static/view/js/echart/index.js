!function (t, e) {
    if ("object" == typeof exports && "object" == typeof module) module.exports = e(); else if ("function" == typeof define && define.amd) define([], e); else {
        var n = e();
        for (var o in n)("object" == typeof exports ? exports : t)[o] = n[o]
    }
}(this, function () {
    return function (t) {
        function e(o) {
            if (n[o])return n[o].exports;
            var r = n[o] = {exports: {}, id: o, loaded: !1};
            return t[o].call(r.exports, r, r.exports, e), r.loaded = !0, r.exports
        }

        var n = {};
        return e.m = t, e.c = n, e.p = "", e(0)
    }([function (t, e, n) {
        t.exports = n(1)
    }, function (t, e) {
        !function (t) {
            function e(t, e) {
                e = $.extend({}, o, e), e.width = e.width || t.offsetWidth, e.height = e.height || t.offsetHeight;
                var r = this;
                r.domElement = t, r.width = e.width, r.height = e.height, r.colors = e.colors || new n(e.startColor, e.endColor, e.colorStep), e.colorFunction && (r.colorFunction = e.colorFunction), e.weightFunction && (r.weightFunction = e.weightFunction), e.labelFunction && (r.labelFunction = e.labelFunction), r.clickHandler = e.clickHandler, r._force = d3.layout.force().gravity(0).charge(0).on("tick", function (t) {
                    r._force_tickHandler.bind(r)(t)
                }).on("end", function (t) {
                    r._tickEnd = !0, r._bubbles.attr("transform", function (t) {
                        return "translate(" + t.x + "," + t.y + ")"
                    })
                }), r._option = e
            }

            function n(t, e, n) {
                startRGB = this.colorRgb(t), startR = startRGB[0], startG = startRGB[1], startB = startRGB[2], endRGB = this.colorRgb(e), endR = endRGB[0], endG = endRGB[1], endB = endRGB[2], sR = (endR - startR) / n, sG = (endG - startG) / n, sB = (endB - startB) / n;
                for (var o = [], r = 0; r < n; r++) {
                    var i = this.colorHex("rgb(" + parseInt(sR * r + startR) + "," + parseInt(sG * r + startG) + "," + parseInt(sB * r + startB) + ")");
                    o.push(i)
                }
                return o
            }

            if ("undefined" == typeof d3)throw new Error("Please include D3.js Library first.");
            var o = {startColor: "", endColor: "", colorStep: 6}, r = {left: 30, right: 30, top: 20, bottom: 20};
            e.prototype = {
                constructor: e,
                domElement: null,
                minRadius: 30,
                maxRadius: 40,
                minFontSize: 10,
                maxFontSize: 18,
                colors: [],
                labelFunction: function (t) {
                    return t.name
                },
                weightFunction: function (t) {
                    return parseInt(t.size)
                },
                colorFunction: function (t) {
                    var e = t.nameCode.substring(t.nameCode.length - 1) - 1;
                    return this.colors[e]
                },
                clickHandler: null,
                customDrawFunction: function (t, e, n, o) {
                    t.append("circle").attr("r", n).style("fill", o).style("opacity", "1")
                },
                collisionPadding: 10,
                minCollisionRadius: 30,
                iconPadding: 150,
                _jitter: .618,
                _force: null,
                _svg: null,
                _ballGroup: null,
                _bubbles: null,
                _legend: null,
                _radiusScale: null,
                _fontSizeScale: null,
                _colorIndexScale: null,
                _width: 0,
                _height: 0,
                _tickEnd: !1,
                _data: null,
                init: function () {
                    var t = this;
                    t._width = t.width - r.right - r.left, t._height = t.height - r.top - r.bottom, t._radiusScale = d3.scale.linear().range([t.minRadius, t.maxRadius]), t._fontSizeScale = d3.scale.linear().range([t.minFontSize, t.maxFontSize]), t._colorIndexScale = d3.scale.linear().range([0, t.colors.length - 1]), t._force.size([t._width, t._height]), d3.select(t.domElement).selectAll("svg").remove(), t._svg = d3.select(t.domElement).append("svg").attr("width", t.width).attr("height", t.height)
                },
                setData: function (t) {
                    var e = this;
                    if (arguments.length < 1)return e._data;
                    e.data = t, e._data = $.extend(!0, [], t);
                    var n = e.labelFunction || n, o = e.weightFunction || o;
                    e._radiusScale.domain([d3.min(e._data, o), d3.max(e._data, o)]), e._fontSizeScale.domain([d3.min(e._data, o), d3.max(e._data, o)]), e._colorIndexScale.domain([d3.min(e._data, o) - 1, d3.max(e._data, o) + 1]), e._svg.selectAll(".node").remove(), e._ballGroup = e._svg.append("g").attr("transform", "translate(" + r.left + "," + r.top + ")").attr("class", "node"), e._bubbles = e._ballGroup.selectAll("g").data(e._data).enter().append("g").style("cursor", "pointer").on("mouseover", function (t) {
                        d3.select(this).select("circle").style("opacity", "0.7")
                    }).on("mouseout", function (t) {
                        d3.select(this).select("circle").style("opacity", "1")
                    }).on("click", function (t) {
                        e.clickHandler instanceof Function && e.clickHandler.call(null, t)
                    });
                    var i = e.colors.length;
                    for (e._bubbles.each(function (t) {
                        var n = o(t), r = e._radiusScale(n), a = e.colorFunction(t);
                        if (!a) {
                            var l = Math.round(e._colorIndexScale(n));
                            a = e.colors[i - l - 1]
                        }
                        return e.customDrawFunction(d3.select(this), t, r, a)
                    }), e._bubbles.append("text").attr({
                        "text-anchor": "middle",
                        dy: ".3em"
                    }).attr("font-size", function (t) {
                        return e._fontSizeScale(o(t))
                    }).attr("fill", "#FFFFFF").attr("font-family", "微软雅黑").text(n), this._data.forEach(function (t, n) {
                        t.fr = Math.max(e.minCollisionRadius, e._radiusScale(o(t)))
                    }), e._tickEnd = !1, e._force.nodes(this._data).start(); !e._tickEnd;)e._force.tick()
                },
                resize: function () {
                    var t = this;
                    t.width = t.domElement.offsetWidth, t.height = t.domElement.offsetHeight, t.init(), t.setData(t.data)
                },
                _force_tickHandler: function (t) {
                    this._bubbles.each(this._gravity(.1 * t.alpha)).each(this._collide(this._jitter))
                },
                _gravity: function (t) {
                    var e = this._width / 2, n = this._height / 2, o = t / 3, r = t;
                    return function (t) {
                        t.x += (e - t.x) * o, t.y += (n - t.y) * r
                    }
                },
                _collide: function (t) {
                    var e = this._data, n = this.collisionPadding;
                    return function (o) {
                        e.forEach(function (e) {
                            if (o !== e) {
                                var r = o.x - e.x, i = o.y - e.y, a = Math.sqrt(r * r + i * i), l = o.fr + e.fr + n;
                                if (a < l) {
                                    a = (a - l) / a * t;
                                    var c = r * a, s = i * a;
                                    o.x -= c, o.y -= s, e.x += c, e.y += s
                                }
                            }
                        })
                    }
                }
            }, n.prototype.colorRgb = function (t) {
                var e = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/, t = t.toLowerCase();
                if (t && e.test(t)) {
                    if (4 === t.length) {
                        for (var n = "#", o = 1; o < 4; o += 1)n += t.slice(o, o + 1).concat(t.slice(o, o + 1));
                        t = n
                    }
                    for (var r = [], o = 1; o < 7; o += 2)r.push(parseInt("0x" + t.slice(o, o + 2)));
                    return r
                }
                return t
            }, n.prototype.colorHex = function (t) {
                var e = t, n = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
                if (/^(rgb|RGB)/.test(e)) {
                    for (var o = e.replace(/(?:(|)|rgb|RGB)*/g, "").split(","), r = "#", i = 0; i < o.length; i++) {
                        var a = Number(o[i]).toString(16);
                        a = a < 10 ? "0" + a : a, "0" === a && (a += a), r += a
                    }
                    return 7 !== r.length && (r = e), r
                }
                if (!n.test(e))return e;
                var l = e.replace(/#/, "").split("");
                if (6 === l.length)return e;
                if (3 === l.length) {
                    for (var c = "#", i = 0; i < l.length; i += 1)c += l[i] + l[i];
                    return c
                }
            }, t && (t.BubbleCloudChart = e)
        }(window)
    }])
});