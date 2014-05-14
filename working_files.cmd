:: Open up emacs with a linked current list of important files for the project.
:: This is for convenience in developing the project.
@echo off
pushd %~dp0
runemacs --load working_files.el
