(ns rosado.fun-test
  (:require [rosado.fun :as f])
  (:require-macros [rosado.fun.macros :refer [do-try]]))


(defn prepend-foo [s] (do-try (str "foo" s)))

(defn capitalized? [s]
  (do-try (every? #(re-matches #"[A-Z]" %) s)))

(defn capitalize [s] (do-try (.toUpperCase s)))

(def composed (comp (bind capitalized?)
                    (bind capitalize)
                    (bind prepend-foo)))
