$(function () {
  $('[data-toggle="control-sidebar"]').controlSidebar();
  $('[data-toggle="push-menu"]').pushMenu();
  var d = $('[data-toggle="push-menu"]').data("lte.pushmenu");
  var a = $('[data-toggle="control-sidebar"]').data("lte.controlsidebar");
  var c = $("body").data("lte.layout");
  var z = ["theme-amber", "theme-bluecyan", "theme-blueindigo", "theme-brown",
    "theme-cyangreen", "theme-deeporange", "theme-deeppurple",
    "theme-deeppurpleblue", "theme-greenteal", "theme-greyblue",
    "theme-indigolightblue", "theme-indigopurple", "theme-purpleamber",
    "theme-purpleorange", "theme-redpink",];

  function y(C) {
    if (typeof (Storage) !== "undefined") {
      return localStorage.getItem(C)
    } else {
      window.alert(
          "Please use a modern browser to properly view this template!")
    }
  }

  function B(C, D) {
    if (typeof (Storage) !== "undefined") {
      localStorage.setItem(C, D)
    } else {
      window.alert(
          "Please use a modern browser to properly view this template!")
    }
  }

  function w(C) {
    $("body").toggleClass(C);
    if ($("body").hasClass("fixed") && C == "fixed") {
      d.expandOnHover();
      c.activate()
    }
    a.fix()
  }

  function x(C) {
    $.each(z, function (D) {
      $("body").removeClass(z[D])
    });
    $("body").addClass(C);
    B("theme", C);
    return false
  }

  function A() {
    var C = y("theme");
    if (C && $.inArray(C, z)) {
      x(C)
    }
    $("[data-theme]").on("click", function (D) {
      if ($(this).hasClass("knob")) {
        return
      }
      D.preventDefault();
      x($(this).data("theme"))
    });
    $("[data-layout]").on("click", function () {
      w($(this).data("layout"))
    });
    $("[data-controlsidebar]").on("click", function () {
      w($(this).data("controlsidebar"));
      var D = !a.options.slide;
      a.options.slide = D;
      if (!D) {
        $(".control-sidebar").removeClass("control-sidebar-open")
      }
    });
    $('[data-enable="expandOnHover"]').on("click", function () {
      $(this).attr("disabled", true);
      d.expandOnHover();
      if (!$("body").hasClass("sidebar-collapse")) {
        $('[data-layout="sidebar-collapse"]').click()
      }
    });
    $('[data-enable="rtl"]').on("click", function () {
      $(this).attr("disabled", true);
      d.expandOnHover();
      if (!$("body").hasClass("rtl")) {
        $('[data-layout="rtl"]').click()
      }
    });
    $('[data-mainsidebarskin="toggle"]').on("click", function () {
      var D = $("body");
      if (D.hasClass("dark-skin")) {
        D.removeClass("dark-skin");
        D.addClass("light-skin")
      } else {
        D.removeClass("light-skin");
        D.addClass("dark-skin")
      }
    });
    if ($("body").hasClass("fixed")) {
      $('[data-layout="fixed"]').attr("checked", "checked")
    }
    if ($("body").hasClass("layout-boxed")) {
      $('[data-layout="layout-boxed"]').attr("checked", "checked")
    }
    if ($("body").hasClass("sidebar-collapse")) {
      $('[data-layout="sidebar-collapse"]').attr("checked", "checked")
    }
    if ($("body").hasClass("rtl")) {
      $('[data-layout="rtl"]').attr("checked", "checked")
    }
  }

  var g = $("<div />", {
    id: "control-sidebar-theme-demo-options-tab",
    "class": "tab-pane active"
  });
  var f = $("<li />", {"class": "nav-item"}).html(
      "<a href='#control-sidebar-theme-demo-options-tab' class='active' data-toggle='tab'  title='Setting'><i class=\"ti-settings\"></i></a>");
  $('[href="#control-sidebar-home-tab"]').parent().before(f);
  var b = $("<div />");
  var e = $("<ul />", {"class": "list-inline clearfix theme-switch"});
  var h = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-amber" style="display: inline-block;vertical-align: middle;" class="clearfix active bg-gradient-amber rounded-circle w-20 h-20" title="Theme Amber"></a>');
  e.append(h);
  var i = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-bluecyan" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-bluecyan rounded-circle w-20 h-20" title="Theme BlueCyan"></a>');
  e.append(i);
  var j = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-blueindigo" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-blueindigo rounded-circle w-20 h-20" title="Theme BlueIndigo"></a>');
  e.append(j);
  var k = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-brown" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-brown rounded-circle w-20 h-20" title="Theme Brown"></a>');
  e.append(k);
  var l = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-cyangreen" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-cyangreen rounded-circle w-20 h-20" title="Theme CyanGreen"></a>');
  e.append(l);
  var m = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-deeporange" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-deeporange rounded-circle w-20 h-20" title="Theme DeepOrange"></a>');
  e.append(m);
  var n = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-deeppurple" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-deeppurple rounded-circle w-20 h-20" title="Theme DeepPurple"></a>');
  e.append(n);
  var o = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-deeppurpleblue" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-deeppurpleblue rounded-circle w-20 h-20" title="Theme DeepPurpleBlue"></a>');
  e.append(o);
  var p = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-greenteal" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-greenteal rounded-circle w-20 h-20" title="Theme GreenTeal"></a>');
  e.append(p);
  var q = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-greyblue" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-greyblue rounded-circle w-20 h-20" title="Theme GreyBlue"></a>');
  e.append(q);
  var r = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-indigolightblue" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-indigolightblue rounded-circle w-20 h-20" title="Theme IndigoLightBlue"></a>');
  e.append(r);
  var s = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-indigopurple" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-indigopurple rounded-circle w-20 h-20" title="Theme IndigoPurple"></a>');
  e.append(s);
  var t = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-purpleamber" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-purpleamber rounded-circle w-20 h-20" title="Theme PurpleAmber"></a>');
  e.append(t);
  var u = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-purpleorange" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-purpleorange rounded-circle w-20 h-20" title="Theme PurpleOrange"></a>');
  e.append(u);
  var v = $("<li />", {style: "padding: 5px;line-height: 25px;"}).append(
      '<a href="javascript:void(0)" data-theme="theme-redpink" style="display: inline-block;vertical-align: middle;" class="clearfix bg-gradient-redpink rounded-circle w-20 h-20" title="Theme RedPink"></a>');
  e.append(v);
  b.append('<h4 class="control-sidebar-heading">Theme Colors</h4>');
  b.append(e);
  b.append(
      '<h4 class="control-sidebar-heading">Background Size</h4><div class="flexbox mb-10 pb-10 bb-1"><label class="control-sidebar-subheading w-p100 mt-5">Choose Size</label><select id="bg-size" class="bg-size custom-select"><option value="wave">Wave</option><option value="half">Half</option><option value="header" selected="">Header</option></select></div>');
  b.append(
      '<h4 class="control-sidebar-heading"></h4><div class="flexbox mb-10 pb-10 bb-1"><label for="toggle_left_sidebar_skin" class="control-sidebar-subheading">Turn Dark/Light</label><label class="switch switch-border switch-danger"><input type="checkbox" data-mainsidebarskin="toggle" id="toggle_left_sidebar_skin"><span class="switch-indicator"></span><span class="switch-description"></span></label></div>');
  b.append(
      '<h4 class="control-sidebar-heading"></h4><div class="flexbox mb-10 pb-10 bb-1"><label for="rtl" class="control-sidebar-subheading">Turn RTL/LTR</label><label class="switch switch-border switch-danger"><input type="checkbox" data-layout="rtl" id="rtl"><span class="switch-indicator"></span><span class="switch-description"></span></label></div>');
  b.append(
      '<h4 class="control-sidebar-heading"></h4><div class="flexbox mb-10"><label for="layout_fixed" class="control-sidebar-subheading">Fixed layout</label><label class="switch switch-border switch-danger"><input type="checkbox" data-layout="fixed" id="layout_fixed"><span class="switch-indicator"></span><span class="switch-description"></span></label></div><div class="flexbox mb-10"><label for="layout_boxed" class="control-sidebar-subheading">Boxed Layout</label><label class="switch switch-border switch-danger"><input type="checkbox" data-layout="layout-boxed" id="layout_boxed"><span class="switch-indicator"></span><span class="switch-description"></span></label></div><div class="flexbox mb-10"><label for="toggle_sidebar" class="control-sidebar-subheading">Toggle Sidebar</label><label class="switch switch-border switch-danger"><input type="checkbox" data-layout="sidebar-collapse" id="toggle_sidebar"><span class="switch-indicator"></span><span class="switch-description"></span></label></div><div class="flexbox mb-10"><label for="toggle_right_sidebar" class="control-sidebar-subheading">Toggle Right Sidebar Slide</label><label class="switch switch-border switch-danger"><input type="checkbox" data-controlsidebar="control-sidebar-open" id="toggle_right_sidebar"><span class="switch-indicator"></span><span class="switch-description"></span></label></div>');
  g.append(b);
  $("#control-sidebar-home-tab").after(g);
  A();
  $('[data-toggle="tooltip"]').tooltip()
});
$(function () {
  $(".theme-switch li a").click(function () {
    $(".theme-switch li a").removeClass("active").addClass("inactive");
    $(this).toggleClass("active inactive")
  })
});
$(function () {
  $(".bg-size").on("change", function () {
    var a = $(this), b = this.value, c = $("body");
    if (b === "wave") {
      $(c).removeClass("onlyheader").addClass("onlywave")
    } else {
      if (b === "header") {
        $(c).removeClass("onlywave").addClass("onlyheader")
      } else {
        $(c).removeClass("onlywave onlyheader")
      }
    }
  })
});