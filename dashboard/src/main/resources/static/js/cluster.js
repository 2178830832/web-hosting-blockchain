const table = $('#cluster')

// table.on('click', 'tr', function () {
//   const data = table.DataTable().row(this).data();
//   alert('You clicked on ' + data[0] + "'s row");
// })

$(function () {
  $.ajax({
    url: '/cluster/list',
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



