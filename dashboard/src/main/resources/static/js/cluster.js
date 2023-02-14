const table = $('#cluster')

$(function () {
  const jqXHR = $.ajax({
    url: '/cluster/list',
    headers: setHeaders('/cluster/list'),
    complete: function () {
      checkAuth(jqXHR)
    },
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



