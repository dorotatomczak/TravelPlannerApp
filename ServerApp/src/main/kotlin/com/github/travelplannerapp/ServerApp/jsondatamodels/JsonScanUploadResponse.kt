package com.github.travelplannerapp.ServerApp.jsondatamodels

class JsonScanUploadResponse (
        val result: ScanUploadResponse
)

enum class ScanUploadResponse{
    OK,
    ERROR
}