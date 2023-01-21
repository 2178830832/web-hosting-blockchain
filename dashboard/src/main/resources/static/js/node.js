const table = $('#website')
const updateModal = $(".modal")
let dataToUpdate = {}

table.on('click', 'tr', function () {
  const data = table.DataTable().row(this).data();
  dataToUpdate['id'] = data.id
  $(".modal #name-label").text('Name: ' + data.name);
  $(".modal #path-label").text('Path: ' + data.location);
  $(".modal #status-button").text(
      data.status === "online" ? "Set offline" : "Set online");
  $(".modal #delete-button").click(function () {
    Swal.fire({
      title: 'Are you sure?',
      text: "You won't be able to revert this!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire(
            'Deleted!',
            'Your website has been deleted.',
            'success'
        )
      }
    })
  });
  $(".modal #update-button").click(function () {
    Swal.fire({
      title: 'Are you sure?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
    }).then((result) => {
      if (result.isConfirmed) {
        dataToUpdate['name'] = $("#name").val()
        dataToUpdate['path'] = $("#path").val()
        $.ajax({
          type: 'POST',
          url: '/website/update',
          contentType: "application/json",
          data: JSON.stringify(dataToUpdate),
          success: function () {
            Swal.fire('Success', 'Your website has been deleted.', 'success')
            .then(function () {
              location.reload()
            })
            location.reload()
          },
          error: function (response) {
            Swal.fire({
              icon: 'error',
              title: 'Unable to update website',
              text: response.responseText,
              allowEnterKey: true,
            })
          }
        });
      }
    })
  })
  updateModal.modal('show');
})

$(document).on("keydown", function (event) {
  if (event.key === "Enter") {
    if (Swal.isVisible()) {
      Swal.close();
    }
  }
});

$("#add-button")[0].onclick = function () {
  Swal.fire({
    title: 'Are you sure?',
    text: "You won't be able to revert this!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    confirmButtonText: 'Yes, delete it!'
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.fire(
          'Deleted!',
          'Your website has been deleted.',
          'success'
      )
    }
  })
}

$(function () {
  $.ajax({
    url: '/website/list',
    success: function (data) {
      table.DataTable({
        data: JSON.parse(data),
        columns: [
          {data: 'id'},
          {data: 'name'},
          {data: 'cid'},
          {data: 'size'},
          {data: 'status'},
          {data: 'createTime'},
          {data: 'updateTime'},
          {data: 'location', visible: false}
        ],
        order: [[0, 'asc']],
        serverSide: false,
        processing: true,
      });
    }
  });
})



