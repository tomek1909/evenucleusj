package com.evenucleus.evenucleus;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.client.Corporation;
import com.evenucleus.client.CorporationRepo;
import com.evenucleus.client.EveApiCaller;
import com.evenucleus.client.EveApiCaller_;
import com.evenucleus.client.Job;
import com.evenucleus.client.JobRepo;
import com.evenucleus.client.JobService;
import com.evenucleus.client.JobSummary;
import com.evenucleus.client.KeyInfoRepo;
import com.evenucleus.client.PendingNotificationRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.PilotRepo;
import com.evenucleus.client.PilotRepo_;
import com.evenucleus.client.PilotService;
import com.evenucleus.client.SettingsRepo;
import com.evenucleus.client.SkillRepo;
import com.evenucleus.client.TypeNameDict;
import com.evenucleus.client.UserData;
import com.evenucleus.client.UserException;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomeks on 2015-01-12.
 */
public class JobServiceT extends TestBase {
    public void test_JobService() throws SQLException, UserException, ApiException {
        TypeNameDict typeNameDict = new TypeNameDict();
        typeNameDict._localdb = _localdb;
        KeyInfoRepo keyInfoRepo = new KeyInfoRepo();
        keyInfoRepo._localdb = _localdb;

        SettingsRepo settingsRepo = new SettingsRepo();
        settingsRepo._localdb = _localdb;

        PilotService service = new PilotService();
        service._typeNameDict = typeNameDict;
        service._eveApiCaller = new EveApiCaller();
        service._keyInfoRepo = keyInfoRepo;
        service._settingsRepo = settingsRepo;

        PilotRepo pilotRepo = new PilotRepo();
        pilotRepo._localdb = _localdb;
        pilotRepo._eveApiCaller = new EveApiCaller();

        CorporationRepo corpoRepo = new CorporationRepo();
        corpoRepo._localdb = _localdb;

        PendingNotificationRepo pendingNotificationRepo = new PendingNotificationRepo();
        pendingNotificationRepo._localdb = _localdb;

        JobRepo jobRepo = new JobRepo();
        jobRepo._localdb = _localdb;
        jobRepo._pilotRepo = pilotRepo;
        jobRepo._pendingNotificationRepo = pendingNotificationRepo;

        JobService jobService = new JobService();
        jobService._pilotRepo = pilotRepo;
        jobService._corporationRepo = corpoRepo;
        jobService._eveApiCaller = new EveApiCaller();
        jobService._typeNameDict = typeNameDict;
        jobService._settingsRepo = settingsRepo;

        SkillRepo skillRepo = new SkillRepo();
        skillRepo._pendingNotificationRepo = pendingNotificationRepo;
        skillRepo._localdb= _localdb;

        keyInfoRepo.AddKey(3692329, "aPQOKWEr0r9bp7yVNVgtx9O9xSPDOgTEXY9FhM93ArndOcE3ZTTV1xGnTDHDoeii");
        keyInfoRepo.AddKey(3231405, "UZDkcXJAQYdDXu8ItoX7ICXT914ephxHX2n07CFjgKwkYhP2XE6PerFGzTWYfgL6");
        keyInfoRepo.AddKey(2812727, "Qw5OES3cKXnLh14qLNJ1BqWa2YvbowvR5lMtHgG3wkqnExToTsraIURLHApLlavC");

        PilotService.Result result = service.Get();
        JobService.Result res = jobService.Get();
        UserData userData = new UserData();
        userData.Pilots = result.pilots;
        userData.Corporations = result.corporations;
        userData.Jobs = res.jobs;
        userData.JobSummary = new JobSummary();

        pilotRepo.Update(userData);
        skillRepo.Update(userData);
        corpoRepo.Update(userData);
        skillRepo.Update(userData);

        Assert.assertTrue(result.cachedUntil.isAfterNow());

        List<Pilot> pilots = pilotRepo.GetAll();
        Assert.assertEquals(6, pilots.size());
        Pilot micio= null;
        for(Pilot p:pilots)
        {
            if (p.Name.equals("MicioGatto")) {micio = p; break;}
            Assert.assertTrue(p.CharacterId != 0);
        }
        Assert.assertNotNull(micio);
        Assert.assertTrue(micio.TrainingActive);
        Assert.assertTrue(micio.Skills.size() > 0);
        Assert.assertTrue(micio.CurrentTrainingEnd.after(new Date()));
        Assert.assertTrue(micio.TrainingQueueEnd.after(new Date()));

        List<Corporation> corporations = corpoRepo.GetAll();
        Assert.assertEquals(1, corporations.size());
        Assert.assertEquals("My Random Corporation", corporations.iterator().next().Name);
    }

}
