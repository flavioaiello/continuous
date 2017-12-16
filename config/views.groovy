listView('1. Build Master') {
    recurse(true)
    jobs {
        regex('.*/build-master/.*')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

listView('2. Build Latest') {
    recurse(true)
    jobs {
        regex('.*/build-latest/.*')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
listView('3. Deploy') {
    recurse(true)
    jobs {
        regex('.*/deploy/.*/.*')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
