(ns frp-examples.index
  (:require [aid.core :as aid]
            [bidi.bidi :as bidi]
            [clojure.string :as str]
            [com.rpl.specter :as s]
            [frp.core :as frp]
            [frp.history :as history]
            [frp-examples.reverse-text :as reverse-text]))

(def route-function
  {:text-reverse reverse-text/reverse-text})

(def route-keywords
  (keys route-function))

(defn unkebab
  [s]
  (str/replace s #"-" ""))

(def example-route
  (zipmap (map (comp unkebab
                     (partial (aid/flip subs) 1)
                     str)
               route-keywords)
          route-keywords))

(def route
  ["/" (merge {"" :index}
              example-route)])

(defn example-component
  [path]
  [:a {:href     path
       :on-click (fn [event*]
                   (.preventDefault event*)
                   (history/push-state {} {} path))}
   [:li (subs path 1)]])

(def index-component
  (->> route-keywords
       (mapv (comp example-component
                   (partial bidi/path-for route)))
       (s/setval s/BEGINNING [:ul])))

(def index
  (frp/behavior index-component))
