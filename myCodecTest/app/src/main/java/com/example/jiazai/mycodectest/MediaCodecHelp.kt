package com.example.jiazai.mycodectest

import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.media.projection.MediaProjection
import android.os.Environment
import android.util.Log
import android.view.Surface
import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by jiazai on 17-11-22.
 */
class MediaCodecHelp: Thread{
    constructor(mediaProjection: MediaProjection?) {
        prepare()
        mMediaProjection = mediaProjection
        mVirtualDisplay = mMediaProjection!!.createVirtualDisplay(TAG+"-display",
                mWidth, mHeight,1, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                mInputSurface, null, null)
    }

    override fun run() = testMediaCodec()


    fun testMediaCodec() {
        try {
            for (i in 1 until maxframe) {
                drainEncoder(false)
            }
            drainEncoder(true)
            Log.d(TAG,"test s")
        } finally {
            release()
        }
    }

    fun quit() {
        mQuit.set(true)
    }

    private fun prepare() {
        mBufferInfo = MediaCodec.BufferInfo()
        val format = MediaFormat.createVideoFormat(mime_type, mWidth,mHeight)

        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        format.setInteger(MediaFormat.KEY_BIT_RATE, mBitrate)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, framerate)
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, iframe)

        mEncoder = MediaCodec.createEncoderByType(mime_type)
        mEncoder!!.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        mInputSurface = mEncoder!!.createInputSurface()
        mEncoder!!.start()

        //Output file
        val outputPath = File(outputdir, "test."+mWidth+"x"+mHeight+".mp4").toString()
        Log.d(TAG,"output file is "+outputPath)

        //create a MediaMuxer.
        //can not add video track and start() here
        //These can only be obtained from the encoder after it has startred processing data
        try {
            Log.d(TAG, "create Muxer")
            mMuxer = MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        } catch (ioe: IOException) {
            throw RuntimeException("MediaMux create fail", ioe)
        }

    }

    private fun release() {
        Log.d(TAG, "release encoder")
        mEncoder?.stop()
        mEncoder?.release()
        mVirtualDisplay?.release()
        mMediaProjection?.stop()
        mInputSurface?.release()
        mMuxer?.stop()
        mMuxer?.release()

    }

    //it is not a good way to use codec
    private fun drainEncoder(endofstream: Boolean) {
        Log.d(TAG, "drainEncoder")

        if (endofstream) {
            Log.d(TAG, "end of stream, sending EOS to encoder")
            mEncoder!!.signalEndOfInputStream()
        }

        // no need to control input stream as InputSurface created
        while (!mQuit.get()) {
            val encoderStatus = mEncoder!!.dequeueOutputBuffer(mBufferInfo, timeout)
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                //no output available yet
                if (!endofstream) {
                    try {
                        Thread.sleep(10)
                    } catch (e: InterruptedException) {}
                    break
                } else {
                    Log.d(TAG, "no output availble, spinning to await EOS")
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                //showld happen before receiving buffers, and only happen once
                resetOutputFormat()
            }  else if (encoderStatus < 0) {
                Log.w(TAG, "unexpected result from encoder.dequeOutputBuffer ")
            } else {
                writeToTrack(encoderStatus)
                mEncoder!!.releaseOutputBuffer(encoderStatus, false)

                if (mBufferInfo!!.flags.and(MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    if (!endofstream) {
                        Log.w(TAG, "reached end of stream unexpectedly")
                    } else {
                        Log.d(TAG, "end of stream reached")
                    }
                    break
                }
            }
        }
    }

    private fun resetOutputFormat() {
        Log.d(TAG,"output format changed")
        if (mMuxerStarted) {
            throw RuntimeException("format change")
        }
        val newFormat = mEncoder!!.getOutputFormat()
        Log.d(TAG, "encoder output format changed "+newFormat)

        //now we have magic Goodies, start the muxer
        mTrackIndex = mMuxer!!.addTrack(newFormat)
        mMuxer!!.start()
        mMuxerStarted = true
    }

    private fun writeToTrack(bufferIndex: Int) {
        val encodedData = mEncoder!!.getOutputBuffer(bufferIndex)
        if (encodedData == null) {
            Log.d(TAG, "output buffer is null")
        }
        if (mBufferInfo!!.flags.and(MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
            /*The codec config data was pulled out  and fed to the muxer
              when we got the INFO_FORMAT_CHANGED status. Ignore it
            */
            Log.d(TAG,"ignoring BUFFER_FLAG_CODEC_CONFIG")
            mBufferInfo!!.size = 0
        }

        if (mBufferInfo!!.size != 0) {
            if (!mMuxerStarted) {
                throw RuntimeException("muxer has not started")
            }

            //adjust the ByteBuffer values to match BufferInfo (really need?)
            encodedData.position(mBufferInfo!!.offset)
            encodedData.limit(mBufferInfo!!.offset+mBufferInfo!!.size)

            //final write data to file
            Log.d(TAG, "sent "+mBufferInfo!!.size+" bytes to muxer")
            mMuxer!!.writeSampleData(mTrackIndex, encodedData, mBufferInfo)
        }

    }

    private val timeout = 10000L
    private val TAG:String = "MediaCodecHelp"
    private var mMuxer: MediaMuxer? = null
    private var mMediaProjection:MediaProjection? = null
    private var mVirtualDisplay:VirtualDisplay? = null
    //this surface should be replaced
    private var mInputSurface: Surface? = null
    private var mBufferInfo: MediaCodec.BufferInfo? = null;
    private var mEncoder: MediaCodec? = null
    private var mQuit = AtomicBoolean(false)
    private val mime_type: String = "video/avc"
    private val framerate = 30
    private val iframe = 1
    private val mWidth = 1920
    private val mHeight = 1080
    private val mBitrate = 5000000
    private var mMuxerStarted = false
    private var mTrackIndex = -1
    private val outputdir = Environment.getExternalStorageDirectory()
    private val maxframe = 150 // 5s
}