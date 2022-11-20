-- $Id: edth.sql 4069 2021-05-18 11:40:25Z yicong $
--
-- Copyright (C) 2008 by The Regents of the University of California
--
-- Redistribution of this file is permitted under the terms of the 
-- BSD license
--
-- Date: 05/15/2008
-- Author: Rares Vernica <rares (at) ics.uci.edu>
-- Maintainer: Yicong Huang <yicongh1 (at) uci.edu>

DROP FUNCTION IF EXISTS edth;
CREATE FUNCTION edth RETURNS INTEGER SONAME 'libedth.so';

SELECT edth('abc', 'ad', 1);
SELECT edth('abc', 'ad', 2);
SELECT edth('abc', 'aaa', 1);
SELECT edth('abc', 'aaa', 2);
SELECT edth('abc', 'abcd', 1);
SELECT edth('abc', 'abcd', 2);
SELECT edth('a', 'abcdefghijklmnopqrstuvwxyz', 2);
