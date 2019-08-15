package com.github.travelplannerapp.jsondatamodels

class JsonScanUploadResponse(
    var result: ScanUploadResponse
)

enum class ScanUploadResponse {
    OK,
    ERROR
}
