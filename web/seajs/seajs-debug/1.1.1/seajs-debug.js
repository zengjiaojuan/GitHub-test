!function () {
    function a(a) {
        return"[object String]" == {}.toString.call(a)
    }

    function b() {
        try {
            return p in m && m[p]
        } catch (a) {
            return!1
        }
    }

    function c(a) {
        return function () {
            var b = Array.prototype.slice.call(arguments, 0);
            b.unshift(k), r.appendChild(k), k.addBehavior("#default#userData"), k.load(p);
            var c = a.apply(o, b);
            return r.removeChild(k), c
        }
    }

    function d(a) {
        return a.replace(u, "___")
    }

    function e(a) {
        for (var b = " \n\r	\f            ​\u2028\u2029　", c = 0, d = a.length; d > c; c++)if (-1 === b.indexOf(a.charAt(c))) {
            a = a.substring(c);
            break
        }
        for (c = a.length - 1; c >= 0; c--)if (-1 === b.indexOf(a.charAt(c))) {
            a = a.substring(0, c + 1);
            break
        }
        return-1 === b.indexOf(a.charAt(0)) ? a : ""
    }

    function f() {
        this.element = null, this._rendered = !1, this.children = ["header", "meta"]
    }

    function g(a) {
        var b = new RegExp("^(http|https|ftp)://([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&amp;%$-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]).(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0).(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0).(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9-]+.)*[a-zA-Z0-9-]+.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(/($|[a-zA-Z0-9.,?'\\+&amp;%$#=~_-]+))*$");
        return b.test(a)
    }

    function h(a, b, c, d) {
        a && (a.addEventListener ? a.addEventListener(b, c, !!d) : a.attachEvent && a.attachEvent("on" + b, c))
    }

    function e(a) {
        for (var b = " \n\r	\f            ​\u2028\u2029　", c = 0, d = a.length; d > c; c++)if (-1 === b.indexOf(a.charAt(c))) {
            a = a.substring(c);
            break
        }
        for (c = a.length - 1; c >= 0; c--)if (-1 === b.indexOf(a.charAt(c))) {
            a = a.substring(0, c + 1);
            break
        }
        return-1 === b.indexOf(a.charAt(0)) ? a : ""
    }

    function i(a, b) {
        A++, l.body ? (l.body.appendChild(a), b && b()) : z > A && setTimeout(function () {
            i(a, b)
        }, 200)
    }

    function j(a, b) {
        if (null == a)return-1;
        var c = 0, d = a.length, e = Array.prototype.indexOf;
        if (e && a.indexOf === e)return a.indexOf(b);
        for (; d > c; c++)if (a[c] === b)return c;
        return-1
    }

    seajs.importStyle("@font-face {    font-family: 'fontello';    src: url('https://i.alipayobjects.com/common/fonts/seajs-debug/fontello.eot');    src: url('https://i.alipayobjects.com/common/fonts/seajs-debug/fontello.eot#iefix') format('embedded-opentype'), /* IE6-IE8 */ url('https://i.alipayobjects.com/common/fonts/seajs-debug/fontello.woff') format('woff'),  /* chrome 6+、firefox 3.6+、Safari5.1+、Opera 11+ */ url('https://i.alipayobjects.com/common/fonts/seajs-debug/fontello.ttf') format('truetype'), /* chrome、firefox、opera、Safari, Android, iOS 4.2+ */ url('https://i.alipayobjects.com/common/fonts/seajs-debug/fontello.svg') format('svg'); /* iOS 4.1- */    font-weight: normal;    font-style: normal;}#seajs-debug-console #seajs-debug-status button,#seajs-debug-console #seajs-debug-meta button,#seajs-debug-console #seajs-debug-map button {    font-family: 'fontello';}#seajs-debug-console, #seajs-debug-console * {    margin: 0;    padding: 0;    border: none;    font: 14px/1.2 Arial}#seajs-debug-console {    position: fixed;    width: 520px;    right: 10px;    bottom: 10px;    border: 2px solid #564F8A;    z-index: 2147483647;    background: #fafafa;}#seajs-debug-console a, #seajs-debug-console a:hover, #seajs-debug-console a:active, #seajs-debug-console a:link {    text-decoration: none;}#seajs-debug-console button {    border: none;    background: transparent;    cursor: pointer;    -webkit-user-select: none;    -moz-user-select: none;    -ms-user-select: none;    -o-user-select: none;    user-select: none;}#seajs-debug-console #seajs-debug-header,#seajs-debug-console #seajs-debug-editor,#seajs-debug-console #seajs-debug-map,#seajs-debug-console #seajs-debug-health {    border: none;    border-bottom: 1px solid lightgrey;}#seajs-debug-console #seajs-debug-header {    margin: 0;    padding: 5px 5px 5px 10px;    height: 32px;    line-height: 20px;    font-weight: bold;    font-size: 16px;    background: #564F8A;    color: #cdbfe3;}#seajs-debug-console #seajs-debug-editor,#seajs-debug-console #seajs-debug-map,#seajs-debug-console #seajs-debug-health {    min-height: 100px;    _height: 100px;    background: #FFF;}#seajs-debug-console #seajs-debug-editor,#seajs-debug-console #seajs-debug-map p input {    font-family: Courier, monospace;    color: #666;}#seajs-debug-console #seajs-debug-editor {    display: block;    width: 510px;    padding: 5px;    resize: vertical;}#seajs-debug-console #seajs-debug-map {    padding: 5px 0;}#seajs-debug-console #seajs-debug-map p {    height: 30px;    line-height: 30px;    overflow: hidden;    padding-left: 10px;}#seajs-debug-console #seajs-debug-map p input {    padding-left: 6px;    height: 24px;    line-height: 24px;    border: 1px solid #dcdcdc;    width: 200px;    vertical-align: middle;    *vertical-align: bottom;}#seajs-debug-console #seajs-debug-map .seajs-debug-hit input {    border-color: #cdbfe3;    background-color: #F6F0FF;}#seajs-debug-console #seajs-debug-map button {    color: #999;}#seajs-debug-console #seajs-debug-map button,#seajs-debug-console #seajs-debug-meta button {    width: 30px;    height: 30px;    line-height: 30px;    text-align: center;}#seajs-debug-console #seajs-debug-status {    height: 35px;}#seajs-debug-console #seajs-debug-status span {    display: inline-block;    *display: inline;    *zoom: 1;    height: 35px;    line-height: 35px;    padding-left: 8px;    color: #AAA;    vertical-align: middle;}#seajs-debug-console #seajs-debug-status button {    width: 35px;    height: 35px;    line-height: 35px;    color: #999;    border: none;    font-size: 16px;    vertical-align: middle;    _vertical-align: top;}#seajs-debug-console #seajs-debug-status button:hover,#seajs-debug-console #seajs-debug-status button.seajs-debug-status-on:hover {    background-color: #f0f0f0;    color: #000;}#seajs-debug-console #seajs-debug-status button:active,#seajs-debug-console #seajs-debug-status button.seajs-debug-status-on {    color: #563d7c;    text-shadow: 0 0 6px #cdbfe3;    background-color: #f0f0f0;}#seajs-debug-console #seajs-debug-action {    float: right;    margin-top: -31px;    *margin-top: -32px;    margin-right: 2px;}#seajs-debug-console #seajs-debug-action button {    position: relative;    z-index: 2;    width: 60px;    height: 28px;    border-radius: 2px;    text-align: center;    color: #333;    background-color: #fff;    border: 1px solid #ccc;    text-transform: uppercase;    *margin-left: 4px;}#seajs-debug-console #seajs-debug-action button:hover,#seajs-debug-console #seajs-debug-action button:focus,#seajs-debug-console #seajs-debug-action button:hover,#seajs-debug-console #seajs-debug-action button:active {  background-color: #ebebeb;  border-color: #adadad;}#seajs-debug-console #seajs-debug-action button:active {    position: relative;    top: 1px;    -webkit-box-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);    -moz-box-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);    box-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);}#seajs-debug-console #seajs-debug-meta {    position: absolute;    right: 0;    top: 0;}#seajs-debug-console #seajs-debug-meta button {    background: #3f386d;    color: white;}#seajs-debug-console #seajs-debug-health {    height: 500px;    /* loading */}/*-webkit-animation: spin 2s infinite linear;-moz-animation: spin 2s infinite linear;-o-animation: spin 2s infinite linear;-ms-animation: spin 2s infinite linear;animation: spin 2s infinite linear;*/#seajs-debug-console.seajs-debug-mini {    width: 30px;    height: 30px;    border: none;}/* ie6 */#seajs-debug-console {    _position: absolute;    _top: expression(documentElement.scrollTop+documentElement.clientHeight-this.clientHeight-5);}* html {    _background: url(null) no-repeat fixed;}");
    var k, l = document, m = window, n = location, o = {}, p = "localStorage", q = "__storejs__";
    if (o.disabled = !1, o.set = function () {
    }, o.get = function () {
    }, o.serialize = function (b) {
        if (a(b))return b;
        var c = [];
        for (var d in b) {
            var f = b[d];
            if (a(f))f = f.replace(/'/g, '"').replace(/\n/g, "\\n").replace(/\r/g, "\\r"), f = "'" + f + "'"; else if (f.hasOwnProperty("length")) {
                for (var g = [], h = 0; h < f.length; h++) {
                    var i = e(f[h][0]), j = e(f[h][1]);
                    i.length && j.length && g.push('["' + i + '","' + j + '"]')
                }
                f = "[" + g.join(",") + "]"
            }
            c.push("" + d + ":" + f)
        }
        return"{" + c.join(",") + "}"
    }, o.deserialize = function (b) {
        if (!a(b))return void 0;
        try {
            return new Function("return " + b)()
        } catch (c) {
            return void 0
        }
    }, b())k = m[p], o.set = function (a, b) {
        return void 0 === b ? o.remove(a) : (k.setItem(a, o.serialize(b)), b)
    }, o.get = function (a) {
        return o.deserialize(k.getItem(a))
    }; else if (l.documentElement.addBehavior) {
        var r, s;
        try {
            s = new ActiveXObject("htmlfile"), s.open(), s.write('<script>document.w=window</script><iframe src="/favicon.ico"></iframe>'), s.close(), r = s.w.frames[0].document, k = r.createElement("div")
        } catch (t) {
            k = l.createElement("div"), r = l.body
        }
        var u = new RegExp("[!\"#$%&'()*+,/\\\\:;<=>?@[\\]^`{|}~]", "g");
        o.set = c(function (a, b, c) {
            return b = d(b), void 0 === c ? o.remove(b) : (a.setAttribute(b, o.serialize(c)), a.save(p), c)
        }), o.get = c(function (a, b) {
            return b = d(b), o.deserialize(a.getAttribute(b))
        })
    }
    try {
        o.set(q, q), o.get(q) != q && (o.disabled = !0), o.remove(q)
    } catch (t) {
        o.disabled = !0
    }
    var v = {debug: !0, show: !0, source: !1, nocache: !1, combo: !1, log: !1, health: !1, mode: !1, custom: "", mapping: []}, w = o.get("seajs-debug-config");
    for (var x in w)v[x] = w[x];
    var y = window, z = 100, A = 0, B = "seajs-debug-", C = B + "status-on", D = B + "mini", E = B + "hit";
    f.prototype.render = function (a, b) {
        var c = this;
        if (this._rendered)return b && b(), void 0;
        this._rendered = !0;
        for (var d = 0; d < a.length; d++)-1 === j(this.children, a[d]) && this.children.push(a[d]);
        this.element = l.createElement("div"), this.element.id = B + "console", this.element.style.display = "none";
        for (var e = "", d = 0; d < this.children.length; d++) {
            var f = this.children[d], g = this["_render" + f.charAt(0).toUpperCase() + f.substring(1)];
            g && (e += g.call(c))
        }
        this.element.innerHTML = e, i(this.element, function () {
            for (var a = 0; a < c.children.length; a++) {
                var d = c.children[a];
                c[d + "Element"] = l.getElementById(B + d);
                var e = c["_bind" + d.charAt(0).toUpperCase() + d.substring(1)];
                e && e.call(c)
            }
            c.element.style.display = "block", c[v.show ? "show" : "hide"](), b && b()
        })
    }, f.prototype._renderHeader = function () {
        return'<h3 id="' + B + 'header" style="display: none;">Sea.js Debug Console</h3>'
    }, f.prototype._renderMap = function () {
        var a = "";
        v.mapping.push(["", "", !0]);
        for (var b = 0; b < v.mapping.length; b++) {
            var c = v.mapping[b];
            a += '<p><input type="text" placeholder="Input source URI" title="Source URI" value="' + c[0] + '" /><button style="cursor: default;">&#xe80c;</button><input type="text" placeholder="Input target URI" title="Target URI" value="' + c[1] + '" /><button data-name="add" ' + (c[2] ? "" : 'style="display: none;"') + '>&#xe804;</button><button data-name="red" ' + (c[2] ? 'style="display: none;"' : "") + ">&#xe805;</button></p>"
        }
        return'<div id="' + B + 'map" style="display: none;">' + a + "</div>"
    }, f.prototype._renderEditor = function () {
        return'<textarea id="' + B + 'editor" style="display: none;">' + v.custom + "</textarea>"
    }, f.prototype._renderStatus = function () {
        this.statusInfo = [
            ["source", "Switch to min files", "Switch to source files", "&#xe80b;"],
            ["combo", "Enable combo", "Disable combo", "&#xe801;"],
            ["nocache", "Enable cache", "Disable cache", "&#xe806;"],
            ["log", "Hide seajs log", "Show seajs log", "&#xe809;"],
            ["mode", "Switch mapping mode", "Switch editor mode", "&#xe808;", function () {
                this.show()
            }]
        ];
        for (var a = "", b = 0; b < this.statusInfo.length; b++) {
            var c = this.statusInfo[b], d = c[0];
            a += "<button " + (v[d] ? 'class="' + C + '"' : "") + ' title="' + c[v[d] ? 1 : 2] + '">' + c[3] + "</button>"
        }
        return'<div id="' + B + 'status" style="display: none;">' + a + "<span></span></div>"
    }, f.prototype._renderAction = function () {
        this.actionInfo = [
            ["Save", function () {
                for (var a = [], b = this.mapElement.getElementsByTagName("input"), c = 0; c < b.length;) {
                    var d = e(b[c].value), f = e(b[c + 1].value);
                    if (d.length && f.length) {
                        if (!g(d))return b[c].focus(), b[c].select(), alert("Invalid URL: " + d), !1;
                        if (!g(f))return b[c + 1].focus(), b[c + 1].select(), alert("Invalid URL: " + f), !1;
                        a.push([d, f])
                    }
                    c += 2
                }
                v.mapping = a;
                try {
                    return new Function("return " + this.editorElement.value + ";")(), v.custom = e(this.editorElement.value), !0
                } catch (h) {
                    return alert("invalid config"), this.editorElement.focus(), !1
                }
            }, !1],
            ["Exit", function () {
                return v.debug = !1, !0
            }, !0]
        ];
        for (var a = "", b = 0; b < this.actionInfo.length; b++) {
            var c = this.actionInfo[b];
            a += "<button>" + c[0] + "</button> "
        }
        return'<div id="' + B + 'action" style="display: none;">' + a + "</div>"
    }, f.prototype._renderMeta = function () {
        this.metaInfo = [
            [v.show, "&#xe80a;", "Go to help", function () {
                y.open("https://github.com/seajs/seajs-debug/issues/4", "_blank")
            }],
            [v.show, "&#xe802;", "Minimize console", function () {
                this.hide()
            }],
            [!v.show, "&#xe803;", "Maximize console", function () {
                this.show()
            }]
        ];
        for (var a = "", b = 0; b < this.metaInfo.length; b++) {
            var c = this.metaInfo[b];
            a += '<button title="' + c[2] + '">' + c[1] + "</button>"
        }
        return'<div id="' + B + 'meta">' + a + "</div>"
    }, f.prototype._renderHealth = function () {
        return'<div id="' + B + 'health" style="display: none;"></div>'
    }, f.prototype._bindMap = function () {
        h(this.mapElement, "click", function (a) {
            var b = a.target || a.srcElement;
            if ("button" === b.tagName.toLowerCase()) {
                var c = b.parentNode, d = c.parentNode;
                if ("add" === b.getAttribute("data-name")) {
                    var e = l.createElement("p");
                    e.innerHTML = c.innerHTML, d.appendChild(e), e.getElementsByTagName("input")[0].focus(), b.style.display = "none", b.nextSibling.style.display = "inline-block"
                } else"red" === b.getAttribute("data-name") && d.removeChild(c)
            }
        })
    }, f.prototype._bindStatus = function () {
        this.statusTipElement = this.statusElement.getElementsByTagName("span")[0];
        for (var a = this, b = this.statusElement.getElementsByTagName("button"), c = 0; c < b.length; c++)!function (b, c) {
            h(b, "click", function () {
                var d = a.statusInfo[c], e = !v[d[0]];
                v[d[0]] = e, a.save(), b.setAttribute("title", d[e ? 1 : 2]), a.statusTipElement.innerHTML = d[e ? 1 : 2], b.className = e ? C : "", d[4] && d[4].call(a, e)
            }), h(b, "mouseover", function () {
                var b = a.statusInfo[c];
                a.statusTipElement.innerHTML = b[v[b[0]] ? 1 : 2]
            }), h(b, "mouseout", function () {
                a.statusTipElement.innerHTML = ""
            })
        }(b[c], c)
    }, f.prototype._bindAction = function () {
        for (var a = this, b = this.actionElement.getElementsByTagName("button"), c = 0; c < b.length; c++)!function (b, c) {
            h(b, "click", function () {
                var b = a.actionInfo[c];
                b[1].call(a) && (a.save(), b[2] ? n.replace(n.href.replace("seajs-debug", "")) : n.reload())
            })
        }(b[c], c)
    }, f.prototype._bindMeta = function () {
        for (var a = this, b = this.metaElement.getElementsByTagName("button"), c = 0; c < b.length; c++)!function (b, c) {
            h(b, "click", function () {
                var b = a.metaInfo[c];
                b[3] && b[3].call(a), a.save()
            })
        }(b[c], c)
    }, f.prototype._bindHealth = function () {
    }, f.prototype.show = function () {
        this.element.className = "", v.show = !0;
        for (var a = 0; a < this.children.length; a++) {
            var b = this.children[a];
            -1 === j(["meta", "health", "editor", "map"], b) && this[b + "Element"] && (this[b + "Element"].style.display = "block")
        }
        v.health ? this.switchTo("health") : v.mode ? (this.switchTo("editor"), this.editorElement.focus()) : this.switchTo("map");
        var c = this.metaElement.getElementsByTagName("button");
        c[0].style.display = "inline-block", c[1].style.display = "inline-block", c[2].style.display = "none"
    }, f.prototype.hide = function () {
        this.element.className = D, v.show = !1;
        for (var a = 0; a < this.children.length; a++) {
            var b = this.children[a];
            "meta" !== b && this[b + "Element"] && (this[b + "Element"].style.display = "none")
        }
        var c = this.metaElement.getElementsByTagName("button");
        c[0].style.display = "none", c[1].style.display = "none", c[2].style.display = "inline-block"
    }, f.prototype.switchTo = function (a) {
        for (var b = ["health", "editor", "map"], c = 0; 3 > c; c++) {
            var d = b[c];
            this[d + "Element"] && (this[d + "Element"].style.display = a === d ? "block" : "none")
        }
    }, f.prototype.save = function () {
        o.set("seajs-debug-config", v)
    }, f.prototype.setHitInput = function (a, b) {
        if (this.mapElement) {
            var c = this.mapElement.getElementsByTagName("p")[a];
            c && b && (c.className = E)
        }
    }, f.prototype.destory = function (a) {
        var b = this["_destory" + a.charAt(0).toUpperCase() + a.substring(1)];
        b && b.call(this)
    };
    var F = new f;
    if (F.config = v, n.search.indexOf("seajs-debug") > -1 && (v.debug = !0), seajs.config({debug: v.debug}), v.debug) {
        if (F.render(["map", "editor", "health", "status", "action"]), l.title = "[Sea.js Debug Mode] - " + l.title, seajs.config({map: [function (a) {
            for (var b = a, c = 0; c < v.mapping.length; c++)v.mapping[c][0].length && v.mapping[c][1] && (a = a.replace(v.mapping[c][0], v.mapping[c][1]), F.setHitInput(c, a !== b));
            return v.source && !/\-debug\.(js|css)+/g.test(a) && (a = a.replace(/\/(.*)\.(js|css)/g, "/$1-debug.$2")), a
        }]}), v.nocache) {
            var G = (new Date).getTime();
            seajs.on("fetch", function (a) {
                if (a.uri) {
                    var b = a.requestUri || a.uri;
                    a.requestUri = (b + (-1 === b.indexOf("?") ? "?t=" : "&t=") + G).slice(0, 2e3)
                }
            }), seajs.on("define", function (a) {
                a.uri && (a.uri = a.uri.replace(/[\?&]t*=*\d*$/g, ""))
            })
        }
        if (v.combo && seajs.config({comboExcludes: /.*/}), v.log && seajs.config({preload: "seajs-log"}), v.health && seajs.config({preload: "seajs-health"}), v.custom) {
            var w = {};
            try {
                w = new Function("return " + v.custom)()
            } catch (t) {
            }
            seajs.config(w)
        }
    }
    if (!seajs.find) {
        var H = seajs.cache;
        seajs.find = function (a) {
            var b = [];
            for (var c in H)if (H.hasOwnProperty(c) && ("string" == typeof a && c.indexOf(a) > -1 || a instanceof RegExp && a.test(c))) {
                var d = H[c];
                d.exports && b.push(d.exports)
            }
            return b
        }
    }
    define("seajs/seajs-debug/1.1.1/seajs-debug", [], {})
}();