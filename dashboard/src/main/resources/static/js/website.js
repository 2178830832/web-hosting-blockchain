const table = $('#website')
const updateModal = $("#update-modal")
const createModal = $("#create-modal")
const createForm = $('#create-form')

table.on('click', 'tr', function () {
  const row = table.DataTable().row(this).data();
  let dataToUpdate = {}
  dataToUpdate['id'] = row.id
  $(".modal #name-label").text('Name: ' + row.name);
  $(".modal #path-label").text('Path: ' + row.location);
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
        $.ajax({
          type: 'POST',
          url: '/website/delete',
          contentType: "application/json",
          data: JSON.stringify(dataToUpdate),
          success: function () {
            Swal.fire('Deleted!', 'Your website has been deleted.', 'success')
            .then(function () {
              location.reload()
            })
          },
          error: function (response) {
            Swal.fire('Unable to delete website', response.responseText, 'error')
          }
        });
      }
    })
  });
  $(".modal #update-button").click(function () {
    dataToUpdate['name'] = $("#name").val()
    dataToUpdate['location'] = $("#path").val()
    const data = JSON.stringify(dataToUpdate)
    Swal.fire({
      title: 'Update website?',
      icon: 'info',
      text: data.slice(1, -1),
      showCancelButton: true,
    }).then((result) => {
      if (result.isConfirmed) {
        $.ajax({
          type: 'POST',
          url: '/website/update',
          contentType: "application/json",
          data: data,
          success: function () {
            Swal.fire('Success!', 'Your website has been updated.', 'success')
            .then(function () {
              location.reload()
            })
          },
          error: function (response) {
            Swal.fire('Unable to update website', response.responseText, 'error')
          }
        });
      }
    })
  })

  updateModal.modal('show');
})

$('.modal').on("hide.bs.modal", function (e) {
  if (Swal.isVisible()) {
    Swal.close();
    e.preventDefault();
  }
});

createForm.submit(function (e) {
  e.preventDefault();
  const formData = JSON.stringify(
      createForm.serializeArray().reduce(function (obj, item) {
        obj[item.name] = item.value;
        return obj;
      }, {})
  );
  Swal.fire({
    title: 'Update website?',
    icon: 'info',
    text: formData.slice(1, -1),
    showCancelButton: true,
  }).then((result) => {
    if (result.isConfirmed) {
      $.ajax({
        type: 'POST',
        url: '/website/insert',
        contentType: 'application/json',
        data: formData,
        success: function () {
          Swal.fire('Success!', 'Your website has been uploaded.', 'success')
          .then(function () {
            location.reload()
          })
        },
        error: function (response) {
          Swal.fire('Unable to upload website', response.responseText, 'error')
        }
      });
    }
  })
})

$("#add-button")[0].onclick = function () {
  $(".modal #create-button").click(function () {
    createForm.validate();
    if (!createForm.valid()) {
      return;
    }
    createForm.submit();
  })

  createModal.modal('show')
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



