const button = document.getElementById('ipfs-button');
// const input = document.getElementById('ipfs-port');
const pace = document.getElementById('test');
//
// button.addEventListener('click', () => {
//   const value = Number(input.value);
//   if (value < 1 || value > 10) {
//     // alert('Please enter a number between 1 and 10');
//     $.toast({
//       heading: "Welcome to my DashboardX Admin",
//       text: "Use the predefined ones, or specify a custom position object.",
//       position: "top-right",
//       loaderBg: "#ff6849",
//       icon: "info",
//       hideAfter: 3000,
//       stack: 6
//     })
//   }
//
//   // button click action
// });

button.onclick = function () {
  console.log('this')
}

pace.onclick = function () {
  // $.ajax({
  //   url: "#", success: function (a) {
  //     $(".ajax-content").html("<hr>Ajax Request Completed !")
  //   }
  // })
  console.log('this')
}

$(function () {
  $(document).ajaxStart(function () {
    Pace.restart()
  });
});

// $(document).ready(function () {
//   $(".tst1").on("click", function () {
//     $.toast({
//       heading: "Welcome to my DashboardX Admin",
//       text: "Use the predefined ones, or specify a custom position object.",
//       position: "top-right",
//       loaderBg: "#ff6849",
//       icon: "info",
//       hideAfter: 3000,
//       stack: 6
//     })
//   });
// });