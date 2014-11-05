(ns rosado.fun
  "Option type for Clojure/ClojureScript.

Use the following factory fns to create appropriate instances.
- rosado.fun/some, rosado.fun/none
- rosado.fun/success, rosado.fun/failure"
  (:refer-clojure :exclude [some map val])
  #+clj
  (:import [com.google.common.base Objects])
  #+cljs
  (:require-macros [rosado.fun.macros :refer [do-try]])
  )

;;; bind (flatmap in Scala)
;;; ('a -> TwoTrack<'b>) -> TwoTrack<'a> -> TwoTrack<'b>

(defprotocol IBind
  (-bind [this f]))

;;; ('a -> 'b) -> TwoTrack<'a> -> TwoTrack<'b>
(defprotocol IFnMap
  (-map [this f]))

;;; Option

(defprotocol Option
  (get-val [this]))

#+clj
(defn- equals [this other]
  (cond
   (identical? this other) true

   (or (nil? other) (not= (class this) (class other))) false

   :default
   (let [^Option self this ^Option opt other]
     (= (get-val self) (get-val opt)))))

(declare ->Some)

(deftype Some [val]
  Option
  (get-val [this] val)

  #+clj java.lang.Iterable
  #+clj
  (iterator [this]
    (.iterator [val]))

  IBind
  (-bind [this f]
    (f val))

  IFnMap
  (-map [this f]
    (-> val f ->Some))

  Object
  (toString [this]
    (str "[" val "]"))

  #+clj
  (hashCode [this]
    (Objects/hashCode val))

  #+clj
  (equals [this other]
    (equals this other))

  #+cljs IEquiv
  #+cljs
  (-equiv [this other]
    (if (identical? this other)
      true
      (= val (.-val other))))

  #+cljs IHash
  #+cljs
  (-hash [this]
    (+ 4861 (hash val))))

(deftype None []
  Option
  (get-val [this] nil)

  #+clj java.lang.Iterable
  #+clj
  (iterator [this]
    (.iterator []))

  IBind
  (-bind [this f]
    this)

  IFnMap
  (-map [this f]
    this)

  Object
  (toString [this] "none")

  #+clj
  (equals [this other]
    (cond
     (identical? this other) true
     (= (class this) (class other)) true
     :default false))

  #+cljs IEquiv
  #+cljs
  (-equiv [this other]
    (if (identical? this other)
      true
      (instance? None other)))

  #+cljs IHash
  #+cljs
  (-hash [this] 5839))

(defn some [val]
  (when (nil? val)
    #+clj (throw (IllegalArgumentException. "argument is null"))
    #+cljs (throw (js/Error. "argument is null")))
  (->Some val))

(def ^:private none-instance (->None))

(defn none [] none-instance)

;; ;;; IResult

(defprotocol IResult
  (result [this]))

(declare ->Success)

(deftype Success [value]
  IResult
  (result [this] value)

  IBind
  (-bind [this f]
    (f value))

  IFnMap
  (-map [this f]
     (-> value f ->Success))

  Object
  #+clj
  (toString [this]
    (-> this Objects/toStringHelper (.add "value" value) str))
  #+cljs
  (toString [this] (str "Sucess[" value "]"))

  #+clj
  (equals [this other]
    (cond
     (identical? this other) true

     (or (nil? other) (not= (class this) (class other))) false

     :default (= value (.-value other))))

  #+clj
  (hashCode [this]
    (Objects/hashCode value))

  #+cljs IEquiv
  #+cljs
  (-equiv [this other]
    (if (identical? this other)
      true
      (= val (.-value other))))

  #+cljs
  IHash
  #+cljs
  (-hash [this]
    (+ 977 (hash val))))

(deftype Failure [value]
  IResult
  (result [this] value)

  IBind
  (-bind [this f]
    this)

  IFnMap
  (-map [this f]
     this)

  #+clj Object
  #+clj
  (toString [this]
    (-> this Objects/toStringHelper (.add "value" value) str))

  #+clj
  (equals [this other]
    (cond
     (identical? this other) true

     (or (nil? other) (not= (class this) (class other))) false

     :default (= value (.-value other))))

  #+clj
  (hashCode [this]
    (Objects/hashCode value))

  #+cljs IEquiv
  #+cljs
  (-equiv [this other]
    (if (identical? this other)
      true
      (= val (.-val other))))

  #+cljs IHash
  #+cljs
  (-hash [this]
    (+ 863 (hash val))))

(defn success [v] (->Success v))

(defn failure [v] (->Failure v))

#+clj
(defmacro do-try
  [exp]
  `(try
     (->Success ~exp)
     (catch Exception ex#
       (->Failure ex#))))

;; ;;; user facing fns

(defn bind
  [f]
  (fn bind* [result]
    (-bind result f)))

(defn map
  [f]
  (fn map* [container]
    (-map container f)))

(defn tee
  "Takes function returning void."
  [f]
  (fn tee* [input]
    (f input)
    input))

