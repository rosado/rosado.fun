(ns rosado.fun.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [rosado.fun :as f])
  (:require-macros [rosado.fun.macros :refer [do-try]]))

(defonce app-state (atom {:text "Hello Chestnut!"}))

(defn test-do-try []
  (do-try (throw (js/Error. "whatever")))
  "great!")

;;; DEMO

(defn prefix-foo [text] (str "foo" text))
(defn capitalize [text] (.toUpperCase text))

(def c2 (comp (f/bind prefix-foo)
              (f/bind capitalize)))

(defn demo-c2 [text]
  (c3 text))

;;; END DEMO

(defn main []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/h1 nil (:text app)))))
    app-state
    {:target (. js/document (getElementById "app"))}))

