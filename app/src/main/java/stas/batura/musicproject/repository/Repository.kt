package stas.batura.musicproject.repository

import stas.batura.musicproject.repository.room.TracksDao

class Repository (private val dataSource : TracksDao) : TracksDao() {



}