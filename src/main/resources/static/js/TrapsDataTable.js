function TrapsDataTable() {
    "use strict";

    var self = this;

    self.init = function () {
        fetchData(0);
    };

    function fetchData(startPage) {
        $.ajax({
            type : "GET",
            url : "/api/traps",
            data: {
                page: startPage,
                size: 10
            },
            success: function(data, textStatus, jqXHR) {
                console.log(textStatus + " " + jqXHR.status);

                if (jqXHR.status === 204) return;
                else if (jqXHR.status !== 200) return;

                $('#clientsInfo').empty().append("Страница " + (data.number + 1) + " из " + data.totalPages);

                var table = $('#trapsTable tbody');
                table.empty();

                $.each(data.content, (i, el) => {
                    let noteRow = '<tr>' +
                        '<td>' + el.regNumber + '</td>' +
                        '</tr>';
                    table.append(noteRow);
                });
            }
        });
    }
}