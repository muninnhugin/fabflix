-- $Id: ed.sql 4069 2021-05-18 11:40:25Z yicong $
--
-- Copyright (C) 2008 by The Regents of the University of California
--
-- Redistribution of this file is permitted under the terms of the
-- BSD license
--
-- Date: 05/15/2008
-- Author: Rares Vernica <rares (at) ics.uci.edu>
-- Maintainer: Yicong Huang <yicongh1 (at) uci.edu>


DROP FUNCTION IF EXISTS ed;
CREATE FUNCTION ed RETURNS INTEGER SONAME 'libed.so';

SELECT ed('abc', 'ad');
SELECT ed('abc', 'aaa');
SELECT ed('abc', 'abcd');
SELECT ed('abc', 'abc');
SELECT ed('abcdefghijklmnopqrstuvwxyz', 'a');
SELECT ed('abc', '');
SELECT ed('', 'abc');
SELECT ed(NULL, 'abc');
SELECT ed('abc', NULL);
