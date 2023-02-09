const table = $('#node')
const updateModal = $("#update-modal")
const updateForm = $('#update-form')
const createModal = $("#create-modal")
const createForm = $('#create-form')

table.on('click', 'tr', function () {
  const row = table.DataTable().row(this).data();
  const dataToUpdate = {}
  dataToUpdate['id'] = row.id
  $(".modal #name-label").text('Node name: ' + row['name'])
  $(".modal #capacity-label").text('Node capacity: ' + row['totalSpace'])

  const onlineButton = $(".modal #online-button")
  if (row['status'] === 'online') {
    onlineButton.text('Shut down node')
  } else {
    onlineButton.text('Open node')
  }

  onlineButton.click(function() {
    Swal.fire({
      title: 'Are you sure?',
      text: "You are changing the status of this node",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#245be8',
      confirmButtonText: 'Confirm'
    }).then((result) => {
      if (result.isConfirmed) {
        const text = $(this).text();

        $.ajax({
          type: "POST",
          url: '/node/status',
          contentType: "application/json",
          data: JSON.stringify(dataToUpdate),
          success: function() {
            Swal.fire('Success!', 'Your node has been updated.', 'success')
            .then(function () {
              location.reload()
            })
          },
          error: function(error) {
            Swal.fire('Unable to change node status', error.responseText, 'error')
          }
        });
      }})

  });

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
          url: '/node/delete',
          contentType: "application/json",
          data: JSON.stringify(dataToUpdate),
          success: function () {
            Swal.fire('Deleted!', 'Your node has been deleted.', 'success')
            .then(function () {
              location.reload()
            })
          },
          error: function (response) {
            Swal.fire('Unable to delete node', response.responseText, 'error')
          }
        });
      }
    })
  });
  $(".modal #update-button").click(function () {
    let formData =
        updateForm.serializeArray().reduce(function (obj, item) {
          obj[item.name] = item.value;
          return obj;
        }, {});
    formData['id'] = row['id'];
    formData['totalSpace'] = formData['capacity']
    delete formData['capacity']
    formData = JSON.stringify(formData)
    Swal.fire({
      title: 'Update node?',
      icon: 'info',
      text: formData.slice(1, -1),
      showCancelButton: true,
    }).then((result) => {
      if (result.isConfirmed) {
        $.ajax({
          type: 'POST',
          url: '/node/update',
          contentType: "application/json",
          data: formData,
          success: function () {
            Swal.fire('Success!', 'Your node has been updated.', 'success')
            .then(function () {
              location.reload()
            })
          },
          error: function (response) {
            Swal.fire('Unable to update node', response.responseText, 'error')
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
    title: 'Create node?',
    icon: 'info',
    text: formData.slice(1, -1),
    showCancelButton: true,
  }).then((result) => {
    if (result.isConfirmed) {
      $.ajax({
        type: 'POST',
        url: '/node/insert',
        contentType: 'application/json',
        data: formData,
        success: function () {
          Swal.fire('Success!', 'Your node has been created.', 'success')
          .then(function () {
            location.reload()
          })
        },
        error: function (response) {
          Swal.fire('Unable to create node', response.responseText, 'error')
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
    url: '/node/list',
    success: function (data) {
      table.DataTable({
        data: JSON.parse(data),
        columns: [
          {data: 'id'},
          {data: 'name'},
          {data: 'status'},
          {data: 'usedSpace'},
          {data: 'totalSpace'},
          {data: 'createTime'},
          {data: 'updateTime'},
        ],
        order: [[0, 'asc']],
        serverSide: false,
        processing: true,
      });
    }
  });
})



