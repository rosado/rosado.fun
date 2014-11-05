(ns rosado.fun.macros)

(defmacro do-try
  [exp]
  `(try
     (rosado.fun/->Success ~exp)
     (catch :default ex#
       (rosado.fun/->Failure ex#))))
