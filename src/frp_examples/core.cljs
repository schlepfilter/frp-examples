(ns frp-examples.core
  (:require [aid.core :as aid]
            [bidi.bidi :as bidi]
            [com.rpl.specter :as s]
            [frp.core :as frp]
            [frp.location :as location]
            [frp-examples.index :as index]
            [reagent.core :as r]))

(enable-console-print!)

(def app
  (aid/=<< (comp (s/setval :index index/index index/route-function)
                 :handler
                 (partial bidi/match-route index/route))
           location/pathname))

(frp/on (partial (aid/flip r/render) (js/document.getElementById "app"))
        app)

(frp/activate)
