$(document).ready(function() {
    updateUploadedExtensions();
    $('#addCustomForm').on('submit', function(e) {
        e.preventDefault();
        addCustom();
    });
    $('#uploadForm').on('submit', function(e) {
        e.preventDefault();
        uploadFile();
    });
});

var uploadedExtensions = new Set();

function updateUploadedExtensions() {
    uploadedExtensions.clear();
    $('#uploadedFiles li').each(function() {
        var fileName = $(this).find('span').text();
        var extension = fileName.split('.').pop().toLowerCase();
        uploadedExtensions.add(extension);
    });
}

function isExtensionUploaded(extension) {
    return uploadedExtensions.has(extension);
}

function checkExtensionUsage(extension, callback) {
    $.ajax({
        url: '/api/files/usage',
        type: 'GET',
        data: { extension: extension },
        success: function(response) {
            callback(response);
        },
        error: function() {
            alert("확장자 사용 여부 확인 중 오류가 발생했습니다.");
        }
    });
}

function toggleFixed(checkbox) {
    const extension = checkbox.id;

    checkExtensionUsage(extension, function(isUsed) {
        if (checkbox.checked && isUsed) {
            alert("해당 확장자는 이미 사용 중입니다. 확인 후 다시 시도해주세요.");
            checkbox.checked = false;
            return;
        }

        var dto = {
            name: extension,
            checked: checkbox.checked
        };

        $.ajax({
            url: '/api/extensions/fixed',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dto),
            success: function() {
                console.log('확장자 ' + extension + '이(가) ' + (checkbox.checked ? '차단' : '해제') + '되었습니다.');
            },
            error: function(response) {
                alert(response.responseText);
                checkbox.checked = !checkbox.checked;
            }
        });
    });
}

function isValidExtension(name) {
    return /^[a-z0-9]+$/.test(name);
}

function addCustom() {
    let extension = $('#customExtension').val().toLowerCase();
    if (!isValidExtension(extension)) {
        alert("확장자는 1자 이상의 영문자나 숫자로만 구성되어야 합니다.");
        return;
    }
    if (isExtensionUploaded(extension)) {
        alert("해당 확장자는 이미 사용 중입니다. 확인 후 다시 시도해주세요.");
        return;
    }

    var dto = {
        name: extension
    };

    $.ajax({
        url: '/api/extensions/custom',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(dto),
        success: function() {
            location.reload();
        },
        error: function(response) {
            alert(response.responseText);
        }
    });
}

function deleteCustom(name) {
    var dto = {
        name: name
    };

    $.ajax({
        url: '/api/extensions/custom',
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(dto),
        success: function() {
            location.reload();
        },
        error: function(response) {
            alert(response.responseText);
        }
    });
}

function uploadFile() {
    const fileInput = $('#fileUpload')[0];
    if (fileInput.files.length === 0) {
        alert("업로드 전 파일을 올린 다음 버튼을 눌러주세요");
        return;
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append('file', file);

    $.ajax({
        url: '/api/files/upload',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            alert(response);
            location.reload();
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText || "파일 업로드 중 오류가 발생했습니다.");
        }
    });
}

function deleteFile(id) {
    $.ajax({
        url: '/api/files/' + id,
        type: 'DELETE',
        success: function() {
            location.reload();
        },
        error: function(response) {
            alert(response.responseText);
        }
    });
}