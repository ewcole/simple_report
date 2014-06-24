;; see http://orgmode.org/worg/org-tutorials/org-publish-html-tutorial.html

(require 'org-publish)

(setq org-publish-cache nil) ;; Publish files whether they've changed or not.

(setq org-publish-project-alist 
      '(("org-notes"
         :base-directory       "."
         :base-extension       "org"
         :publishing-directory "./md"
         :recursive            t
         :publishing-function  org-md-export-as-markdown
         :headline-levels      4
         :auto-preamble        nil
         :auto-sitemap         t
         :sitemap-filename     "sitemap.org"
         :sitemap-title        "Documentation"
         )
        ("org-static"
         :base-directory       "."
         :base-extension       "css\\|js\\|png\\|jpg\\|svg"
         :publishing-directory "./md"
         :recursive            t
         :publishing-function  org-publish-attachment)
        ("org" :components ("org-notes" "org-static")))
      )
(switch-to-buffer "*Messages*")
(org-publish-project "org") 
(message "**************************************************")
(message "** DONE                                         **")
(message "**************************************************")
;(save-buffers-kill-terminal)
