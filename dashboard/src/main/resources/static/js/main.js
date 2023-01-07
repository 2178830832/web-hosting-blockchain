$(document).ready(function () {
  const t = $('#websites').DataTable({
    processing: true,
    serverSide: true,
    ajax: {
      url: '/test',
      dataSrc: 'data'
    },
    columns: [
      {data: 'cid'},
      {data: 'cid'},
      {data: 'componentType'},
      {data: 'location'},
      {data: 'online'},
      {data: 'typeAsString'},
      {data: 'value'},
    ],
    columnDefs: [
      {
        searchable: false,
        orderable: false,
        targets: 0,
      },
    ],
    order: [[1, 'asc']],
  });

});