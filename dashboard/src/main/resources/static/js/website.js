$(function() {
  const t = $('#website').DataTable({
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
    order: [[0, 'asc']],
  });

});