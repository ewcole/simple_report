(defun get-re-match (num)
  "Get the numbered match from re match"
  (let ((start (match-beginning num))
        (end   (match-end num)))
    (buffer-substring-no-properties start end)
    )
  )

;; Create a document, working_files.org, with links to all Groovy files.
(let ((work-buffer-name "working_files.org"))
  (find-file work-buffer-name)
  (erase-buffer)
  (shell-command "gradle clean" "*Messages*")
  (shell-command "dir /s /b *.groovy" work-buffer-name "*Messages*")
  (while (re-search-forward "\\(.*\\\\\\([A-Za-z0-9]+\.groovy\\)\\)" nil t)
    (insert (concat "- [[file:" (get-re-match 1) "][" (get-re-match 2) "]]"))
    (delete-region (match-beginning 0) (match-end 0)))
  (goto-char 0)
  ;; find the first \src\main\ line and create Source header
  (progn 
    (search-forward "\\src\\main\\" nil t)
    (search-backward "\n")
    (insert "\n* Source"))
  ;; find the first \src\test\ line and create Test header
  (progn 
    (search-forward "\\src\\test\\" nil t)
    (search-backward "\n")
    (insert "\n* Test"))
  ;; Save this file.
  (save-current-buffer)
)
