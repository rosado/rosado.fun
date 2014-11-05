(ns rosado.fun-test
  (:require [clojure.test :refer :all]
            [rosado.fun :as f]))

(deftest basic-functionality
  (testing "equality"
    (are [cmp x y] (cmp x y)
      = (f/some 2) (f/some 2)
      = (f/none) (f/none)
      = (f/failure :some-error) (f/failure :some-error)
      = (f/success 1) (f/success 1)
      not= (f/success 1) (f/success 2)
      not= (f/failure (Exception. "foo")) (f/failure (Exception. "other"))))
  (testing "map"
    (is (= ((f/map inc) (f/some 1)) (f/some 2)))))

;;; composition etc

;;;  we take a couple of 2 track fns (functions returning
;;;  Success|Failure)
(defn prepend-foo [^String s] (f/do-try (str "foo" s)))

(defn capitalize [^String s] (f/do-try (.toUpperCase s)))

(defn capitalized? [s]
  (f/do-try (every? #(Character/isUpperCase %) s)))

(def composed (comp (f/bind capitalized?)
                    (f/bind capitalize)
                    (f/bind prepend-foo)))

(deftest compose-2-track-fns
  (is (true? (f/result (composed (f/some "foo")))))
  (is (false? (f/result (composed (f/some "111")))))
  ;; this one will throw (but will be caught via do-try)
  (is (false? (f/result (composed (f/some (Object.)))))))

