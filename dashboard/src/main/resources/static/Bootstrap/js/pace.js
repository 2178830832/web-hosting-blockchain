$(function () {
  $(document).ajaxStart(function () {
    Pace.restart()
  });
  $(".ajax").click(function () {
    $.ajax({
      url: "#", success: function (a) {
        $(".ajax-content").html("<hr>Ajax Request Completed !")
      }
    })
  })
});