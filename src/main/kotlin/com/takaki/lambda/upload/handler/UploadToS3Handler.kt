package com.takaki.lambda.upload.handler

import java.io.*
import java.net.URL
import java.util.zip.GZIPInputStream

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata


@Suppress("DEPRECATION")
class UploadToS3Handler {

    @Throws(IOException::class)
    fun handler(context: Context): String
    {
        try {
            val logger = context.logger

            // set S3 object metadata when uploading
            val metaData = ObjectMetadata()
            metaData.setHeader("Cache-Control", "max-age=86400")
            metaData.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")

            // get .gz file
            val url = URL("http://hoge.com/fuga.gz")
            val conn = url.openConnection()
            val dataInputStream = DataInputStream(conn.getInputStream())
            // read .gz file input stream
            val targetFileInputStream = GZIPInputStream(dataInputStream)

            // get your S3 client
            val client = AmazonS3Client()
            client.putObject(PutObjectRequest("Bucket Name", "File Name (Object Key)", targetFileInputStream, metaData))

        } catch (e: Exception) {
            return "Uploading is failed"
        }

        return "Uploading is successful"
    }
}