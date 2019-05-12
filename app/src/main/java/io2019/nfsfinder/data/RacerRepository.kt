package io2019.nfsfinder.data

class RacerRepository {
    val racerMap: Map<Int, Racer> = HashMap()
    private var refreshTime: Int = 3000 //frequency of updates in milliseconds
}