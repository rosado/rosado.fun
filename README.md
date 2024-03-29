# rosado.fun

Option/None type for Clojure/ClojureScript.

See this [presentation][rail] for motivation (or don't if you know what 
monads are).

## Usage

See `rosado.fun-test` ns for examples.

## Implementation Notes

Since Clojure doesn't have algebraic data types, I fake them with protocols 
and deftype.

## Development

Start a REPL (in a terminal: `lein repl`, or from Emacs: open a
clj/cljs file in the project, then do `M-x cider-jack-in`. Make sure
CIDER is up to date).

In the REPL do

```clojure
(run)
(browser-repl)
```

The call to `(run)` does two things, it starts the webserver at port
10555, and also the Figwheel server which takes care of live reloading
ClojureScript code and CSS. Give them some time to start.

Running `(browser-repl)` starts the Weasel REPL server, and drops you
into a ClojureScript REPL. Evaluating expressions here will only work
once you've loaded the page, so the browser can connect to Weasel.

When you see the line `Successfully compiled "resources/public/app.js"
in 21.36 seconds.`, you're ready to go. Browse to
`http://localhost:10555` and enjoy.

**Attention: It is not longer needed to run `lein figwheel`
  separately. This is now taken care of behind the scenes**

## Trying it out

If all is well you now have a browser window saying 'Hello Chestnut',
and a REPL prompt that looks like `cljs.user=>`.

Open `resources/public/css/style.css` and change some styling of the
H1 element. Notice how it's updated instantly in the browser.

Open `src/cljs/rosado.fun/core.cljs`, and change `dom/h1` to
`dom/h2`. As soon as you save the file, your browser is updated.

In the REPL, type

```
(ns rosado.fun.core)
(swap! app-state assoc :text "Interactivity FTW")
```

Notice again how the browser updates.

## License

Copyright © 2014 Roland Sadowski

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.


[rail]: http://vimeo.com/97344498