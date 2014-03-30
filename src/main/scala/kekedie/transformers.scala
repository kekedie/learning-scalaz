package kekedie

import scalaz._
import scalaz.Scalaz._

case class Song(name: String, durationInSeconds: Int)
case class Album(name: String, songs: List[Song])
case class Artist(name: String, albums: List[Album])
case class SongLookup(artistName: String, albumName: String, songName: String)

object transformers extends App {

  def findSongs(artists: List[Artist], lookups: List[SongLookup]): OptionT[IntState, List[Song]] = {
    lookups.traverseU { lookup =>
      findSong(artists, lookup.artistName, lookup.albumName, lookup.songName)
    }
  }

  def findSong(artists: List[Artist],
               artistName: String,
               albumName: String,
               songName: String): OptionT[IntState, Song] = {
    for {
      artist <- OptionT.optionT(artists.find(_.name === artistName).pure[IntState])
      album  <- OptionT.optionT(artist.albums.find(_.name === albumName).pure[IntState])
      song   <- OptionT.optionT(album.songs.find(_.name === songName).pure[IntState])
      _      <- modify { count: Int =>
        if (song.durationInSeconds >= 10*60) count + 1 else count
      }.liftM[OptionT]
    } yield song
  }

  type IntState[+A] = State[Int, A]

  val song1_1 = Song("song1_1",  100)
  val song1_2 = Song("song1_2",  600)
  val song1_3 = Song("song1_3", 1200)
  val album1 = Album("album1", List(song1_1, song1_2, song1_3))

  val song2_1 = Song("song2_1",  100)
  val song2_2 = Song("song2_2",  600)
  val song2_3 = Song("song2_3", 1200)
  val album2 = Album("album2", List(song2_1, song2_2, song2_3))

  val artist1 = Artist("artist1", List(album1, album2))

  println(findSongs(List(artist1), List(SongLookup("artist1", "album1", "song1_3"))).run(0))

}
