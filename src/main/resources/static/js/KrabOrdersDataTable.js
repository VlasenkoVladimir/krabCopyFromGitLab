function KrabOrdersDataTable() {
    "use strict";

    var self = this;

    self.init = function () {
        fetchData(0);
    };

    function fetchData(startPage) {
        $.ajax({
            type : "GET",
            url : "/api/kraborders",
            data: {
                page: startPage,
                size: 10
            },
            success: function(data, textStatus, jqXHR) {
                console.log(textStatus + " " + jqXHR.status);

                if (jqXHR.status === 204) return;
                else if (jqXHR.status !== 200) return;

                $('#krabOrdersInfo').empty().append("Страница " + (data.number + 1) + " из " + data.totalPages);

                var table = $('#krabOrdersTable tbody');
                table.empty();

                $.each(data.content, (i, el) => {
                    var statusStyle = "";

                    switch(el.status) {
                        case 'OPENED':
                            statusStyle = "table-success";
                            break;
                        case 'WARNING':
                            statusStyle = "table-warning";
                            break;
                        case 'EXPIRED':
                            statusStyle = "table-danger";
                            break;
                        case 'CLOSED_EXPIRED':
                            statusStyle = "table-light";
                            break;
                        case 'CLOSED':
                            statusStyle = "table-default";
                            break;
                    }

                    if (el.dateEnd == null) el.dateEnd = "Нет данных";

                    let noteRow = '<tr style="cursor: pointer;" class="' + statusStyle + '" onclick="window.location.href = \'/traps\';">' +
                        '<td>' + el.id + '</td>' +
                        '<td>' + el.expirationDaysNumber + '</td>' +
                        '<td>' + el.regDate + '</td>' +
                        '<td>' + el.beginDate + '</td>' +
                        '<td>' + el.endDate + '</td>' +
                        '<td><a class="link-dark" href="/clients">' + el.client.lastName + " " + el.client.firstName + " " + el.client.middleName + '</a></td>' +
                        '</tr>';
                    table.append(noteRow);
                });
            }
        });
    }
}